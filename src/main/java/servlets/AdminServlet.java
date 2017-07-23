package servlets;

import entities.FlightEntity;
import repository.FlightRepository;
import util.AdminValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private AdminValidator validator = new AdminValidator();

    private HashMap<String, String> filterSortStorage = new HashMap<>(6);

    private FlightRepository repository = new FlightRepository();

    private Comparator<FlightEntity> sortByDate = Comparator.comparing(FlightEntity::getDate);
    private Comparator<FlightEntity> sortByTime = Comparator.comparing(FlightEntity::getTime);
    private Comparator<FlightEntity> sortByFlightNumber = Comparator.comparing(FlightEntity::getFlightNumber);
    private Comparator<FlightEntity> sortByDirection = Comparator.comparing(FlightEntity::getDirectionType);
    private Comparator<FlightEntity> sortByWaypoint = Comparator.comparing(FlightEntity::getWaypoint);
    private Comparator<FlightEntity> sortByTerminal = Comparator.comparing(FlightEntity::getTerminal);
    private Comparator<FlightEntity> sortByBoard = Comparator.comparing(FlightEntity::getBoardId);


    private Predicate<FlightEntity> directionFilter(final int directionType) {
        return flightEntity -> flightEntity.getDirectionType() != directionType;
    }
    private Predicate<FlightEntity> dateFilter(final LocalDate beginDate, final LocalDate endDate) {
        return flightEntity -> beginDate.minusDays(1).isBefore(flightEntity.getDate()) && endDate.plusDays(1).isAfter(flightEntity.getDate());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!validator.validate(request))
            request.getRequestDispatcher("login").forward(request, response);

        List<FlightEntity> flightList = repository.getAll();

        filter(request, response);
        if (request.getParameter("sort") != null) sort(request, response);

        request.setAttribute("storage", filterSortStorage);
        request.setAttribute("list", flightList);
        request.getRequestDispatcher("admin.jsp").forward(request, response);
    }

    private void filter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<FlightEntity> flightList = repository.getAll();

        String directionFilter = request.getParameter("directionFilter");
        directionFilter = directionFilter != null ? directionFilter : "all";

        String beginDate = request.getParameter("beginDate");
        beginDate = beginDate != null ? beginDate : LocalDate.now().minusDays(1).toString();

        String endDate = request.getParameter("endDate");
        endDate = endDate != null ? endDate : LocalDate.now().plusDays(1).toString();

        filterSortStorage.put("directionFilter", directionFilter);
        filterSortStorage.put("beginDate", beginDate);
        filterSortStorage.put("endDate", endDate);

        flightList = flightList
                .stream()
                .filter(directionFilter.equals("arrive") ? directionFilter(0) : (directionFilter.equals("leave") ? directionFilter(1) : directionFilter(-1)))
                .filter(!(beginDate.equals("") && endDate.equals("")) ? dateFilter(LocalDate.parse(beginDate), LocalDate.parse(endDate)) : directionFilter(Byte.parseByte(directionFilter)))
                .collect(Collectors.toList());

        request.setAttribute("storage", filterSortStorage);
        request.setAttribute("list", flightList);
        request.getRequestDispatcher("admin.jsp").forward(request, response);
    }

    private void sort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<FlightEntity> flightList = repository.getAll();

        String sort = request.getParameter("sort");
        filterSortStorage.put("sort", sort);

        flightList = flightList
                .stream()
                .sorted(sort.equals("date") ? sortByDate : (sort.equals("time") ? sortByTime : (sort.equals("flightNumber") ? sortByFlightNumber : (sort.equals("direction") ? sortByDirection : (sort.equals("waypoint") ? sortByWaypoint : (sort.equals("terminal") ? sortByTerminal : sortByBoard))))))
                .collect(Collectors.toList());

        request.setAttribute("storage", filterSortStorage);
        request.setAttribute("list", flightList);
        request.getRequestDispatcher("admin.jsp").forward(request, response);
    }
}
