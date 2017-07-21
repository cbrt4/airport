package servlets;

import entities.FlightEntity;
import repository.FlightRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/info")
public class InfoServlet extends HttpServlet {

    //TODO: simplify filterSort() method!!!

    private FlightRepository repository = new FlightRepository();

    private Comparator<FlightEntity> sortByTime = Comparator.comparing(FlightEntity::getTime);

    private Comparator<FlightEntity> sortByFlightNumber = Comparator.comparing(FlightEntity::getFlightNumber);

    private Comparator<FlightEntity> sortByWaypoint = Comparator.comparing(FlightEntity::getWaypoint);

    private Comparator<FlightEntity> sortByTerminal = Comparator.comparing(FlightEntity::getTerminal);

    private Comparator<FlightEntity> sortByGate = Comparator.comparing(FlightEntity::getGate);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("directionFilter") != null ||
                request.getParameter("dateFilter") != null ||
                request.getParameter("sort") != null) filterSort(request, response);
        else {
            List<FlightEntity> flightList = repository.getAll()
                    .stream()
                    .filter(flightEntity -> flightEntity.getDirectionType() == 0)
                    .sorted(sortByTime)
                    .collect(Collectors.toList());
            request.setAttribute("direction", "leaving");
            request.setAttribute("date", "today");
            request.setAttribute("list", flightList);
            request.getRequestDispatcher("info.jsp").forward(request, response);
        }
    }

    private void filterSort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<FlightEntity> flightList = repository.getAll();

        String directionFilter = request.getParameter("directionFilter");

        if (directionFilter != null && directionFilter.equals("arrive")) {
            flightList = flightList
                    .stream()
                    .filter(flightEntity -> flightEntity.getDirectionType() == 1)
                    .sorted(sortByTime)
                    .collect(Collectors.toList());
            request.setAttribute("direction", "arriving");
        }

        if (directionFilter != null && directionFilter.equals("leave")) {
            flightList = flightList
                    .stream()
                    .filter(flightEntity -> flightEntity.getDirectionType() == 0)
                    .sorted(sortByTime)
                    .collect(Collectors.toList());
            request.setAttribute("direction", "leaving");
        }

        String dateFilter = request.getParameter("dateFilter");

        if (dateFilter != null && dateFilter.equals("yesterday")) {
            flightList = flightList
                    .stream()
                    .filter(flightEntity -> flightEntity.getDate().equals(LocalDate.now().minusDays(1)))
                    .sorted(sortByTime)
                    .collect(Collectors.toList());
            request.setAttribute("date", "yesterday");
        }

        if (dateFilter != null && dateFilter.equals("today")) {
            flightList = flightList
                    .stream()
                    .filter(flightEntity -> flightEntity.getDate().equals(LocalDate.now()))
                    .sorted(sortByTime)
                    .collect(Collectors.toList());
            request.setAttribute("date", "today");
        }

        if (dateFilter != null && dateFilter.equals("tomorrow")) {
            flightList = flightList
                    .stream()
                    .filter(flightEntity -> flightEntity.getDate().equals(LocalDate.now().plusDays(1)))
                    .sorted(sortByTime)
                    .collect(Collectors.toList());
            request.setAttribute("date", "tomorrow");
        }

        String sort = request.getParameter("sort");

        if (sort != null && sort.equals("time")) {
            flightList = flightList.stream()
                    .sorted(sortByTime)
                    .collect(Collectors.toList());
        }

        if (sort != null && sort.equals("flightNumber")) {
            flightList = flightList.stream()
                    .sorted(sortByFlightNumber)
                    .collect(Collectors.toList());
        }

        if (sort != null && sort.equals("waypoint")) {
            flightList = flightList.stream()
                    .sorted(sortByWaypoint)
                    .collect(Collectors.toList());
        }

        if (sort != null && sort.equals("terminal")) {
            flightList = flightList.stream()
                    .sorted(sortByTerminal)
                    .collect(Collectors.toList());
        }

        if (sort != null && sort.equals("gate")) {
            flightList = flightList.stream()
                    .sorted(sortByGate)
                    .collect(Collectors.toList());
        }

        request.setAttribute("list", flightList);
        request.getRequestDispatcher("info.jsp").forward(request, response);
    }
}