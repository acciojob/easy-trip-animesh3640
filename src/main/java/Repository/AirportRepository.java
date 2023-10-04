package Repository;

import com.driver.model.Airport;
import java.util.*;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Repository
public class AirportRepository {

    HashMap<String , Airport> airportDb = new HashMap<>();
    HashMap<Integer, Flight>flightDb=new HashMap<>();

    HashMap<Integer,Passenger>passengerDb=new HashMap<>();
    //HashMap<Integer, HashSet<Passenger>> flightPassengerDb=new HashMap<>();
    HashMap<Integer,List<Integer>> flightToPassengerDb = new HashMap<>();
    HashSet<Integer> bookedPassenger=new HashSet<>();


    public String addAirport(Airport airport){

        /*String key = airport.getAirportName();

        airportDb.put(key,airport);

        return "Airport Added Successfully";*/

        airportDb.put(airport.getAirportName(),airport);
        return "SUCCESS";
    }
    public String getLargestAirportName(){

        List<String> list=new ArrayList<>();
        int max=Integer.MIN_VALUE;
        for(Airport air:airportDb.values()){
            if(air.getNoOfTerminals()>max){
                max= air.getNoOfTerminals();
            }
        }
        for(Airport air:airportDb.values()){
            if(air.getNoOfTerminals()==max){
                list.add(air.getAirportName());
            }
        }
        if(list.size()>1)
            Collections.sort(list);
        return list.get(0);
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity){

        double shortest=Integer.MAX_VALUE;
        for(Flight f:flightDb.values()){
            if(f.getFromCity()==fromCity && f.getToCity()==toCity){
                shortest=Math.min(shortest,f.getDuration());
            }
        }
        if(shortest==Integer.MAX_VALUE){
            return -1;
        }
        return shortest;
    }

    public int getNumberOfPeopleOn( Date date, String airportName){

        Airport airport = airportDb.get(airportName);
        if(Objects.isNull(airport)){
            return 0;
        }
        City city = airport.getCity();
        int count = 0;
        for(Flight flight:flightDb.values()){
            if(date.equals(flight.getFlightDate()))
                if(flight.getToCity().equals(city) || flight.getFromCity().equals(city)){

                    int flightId = flight.getFlightId();
                    count = count + flightToPassengerDb.get(flightId).size();
                }
        }
        return count;

    }
    public int calculateFlightFare(Integer flightId){

       /* Flight flight=flightDb.get(flightId);
        int fare=3000 + flight.getTicketsBooked() * 50 ;


        return fare;*/
        return 3000+(flightToPassengerDb.get(flightId).size()*50);

    }


    public String bookATicket(Integer flightId,Integer passengerId){

        if(Objects.nonNull(flightToPassengerDb.get(flightId)) &&
                (flightToPassengerDb.get(flightId).size() < flightDb.get(flightId).getMaxCapacity())){


            List<Integer> passengers =  flightToPassengerDb.get(flightId);

            if(passengers.contains(passengerId)){
                return "FAILURE";
            }

            passengers.add(passengerId);
            flightToPassengerDb.put(flightId,passengers);
            return "SUCCESS";
        }
        else if(Objects.isNull(flightToPassengerDb.get(flightId))){
            flightToPassengerDb.put(flightId,new ArrayList<>());
            List<Integer> passengers =  flightToPassengerDb.get(flightId);

            if(passengers.contains(passengerId)){
                return "FAILURE";
            }

            passengers.add(passengerId);
            flightToPassengerDb.put(flightId,passengers);
            return "SUCCESS";

        }
        return "FAILURE";
    }

    public String cancelATicket(Integer flightId,Integer passengerId){

        List<Integer> passengers = flightToPassengerDb.get(flightId);
        if(passengers == null)
            return "FAILURE";

        if(passengers.contains(passengerId)){
            passengers.remove(passengerId);
            return "SUCCESS";
        }
        return "FAILURE";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){

        int count=0;
        for(List<Integer> list:flightToPassengerDb.values()){
            for(int x:list){
                if(x==passengerId){
                    count++;
                }
            }
        }
        return count;
    }

    public String addFlight(Flight flight){

        /*int key=flight.getFlightId();
        flightDb.put(key,flight);

        return "Successful";*/

        flightDb.put(flight.getFlightId(),flight);
        return "SUCCESS";
    }

    public String getAirportNameFromFlightId(Integer flightId){

        if (flightDb.containsKey(flightId)) {
            City city = flightDb.get(flightId).getFromCity();
            for (Airport airport : airportDb.values()) {
                if (airport.getCity().equals(city)) {
                    return airport.getAirportName();
                }
            }
        }
        return null;
    }

    public int calculateRevenueOfAFlight(Integer flightId){


        /*Flight flight=flightDb.get(flightId);
        int tickets=flight.getTicketsBooked();
        int revenue=0;
        for(int i=0 ;i < tickets ; i++){
            revenue+=3000+i*50;
        }


        return revenue;*/

        return (flightToPassengerDb.get(flightId).size() * (flightToPassengerDb.get(flightId).size() - 1))*25 + 3000*flightToPassengerDb.get(flightId).size() ;
    }

    public String addPassenger(Passenger passenger){

        /*int key = passenger.getPassengerId();

        passengerDb.put(key,passenger);

        return "Successful";*/
        passengerDb.put(passenger.getPassengerId(),passenger);
        return "SUCCESS";
    }
}