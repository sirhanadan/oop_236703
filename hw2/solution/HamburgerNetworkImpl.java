package OOP.Solution;

import OOP.Provided.HungryStudent;
import OOP.Provided.Restaurant;
import OOP.Provided.HamburgerNetwork;

import OOP.Provided.HungryStudent.*;
import OOP.Provided.Restaurant.*;
import OOP.Provided.HamburgerNetwork.*;

import OOP.Provided.HamburgerNetwork.ImpossibleConnectionException;
import OOP.Provided.HungryStudent;
import OOP.Provided.HungryStudent.*;
import OOP.Provided.Restaurant;
import OOP.Provided.Restaurant.RateRangeException;
import OOP.Provided.Restaurant.RestaurantAlreadyInSystemException;
import OOP.Provided.Restaurant.RestaurantNotInSystemException;
import OOP.Provided.HungryStudent.ConnectionAlreadyExistsException;
import OOP.Provided.HungryStudent.SameStudentException;
import OOP.Provided.HungryStudent.StudentAlreadyInSystemException;
import OOP.Provided.HungryStudent.StudentNotInSystemException;
import OOP.Provided.HungryStudent.UnratedFavoriteRestaurantException;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;



/**
 * Each instance of the HamburgerNetwork class has holds a collection of registered students,
 * a collection of registered restaurants, and manages different relations between
 * the two.
 * */
public class HamburgerNetworkImpl implements HamburgerNetwork {

    private Set<HungryStudent> students;
    private Set<Restaurant> restaurants;
    
    public HamburgerNetworkImpl() {
    	this.students = new HashSet<>();
    	this.restaurants = new HashSet<>();
    }
    
    @Override public HungryStudent joinNetwork(int id, String name) throws StudentAlreadyInSystemException{
    	HungryStudent s = new HungryStudentImpl(id, name);
    	if(students.contains(s)) {
    		throw new StudentAlreadyInSystemException();
    	}
    	students.add(s);
    	return s;
    }
    
    @Override public Restaurant addRestaurant(int id, String name, int dist, Set<String> menu) 
    		throws RestaurantAlreadyInSystemException{
    	Restaurant r = new RestaurantImpl(id, name, dist, menu);
    	if(restaurants.contains(r)) {
    		throw new RestaurantAlreadyInSystemException();
    	}
    	restaurants.add(r);
    	return r;
    	
    }
    
    public int numberOfStudents() {
    	return this.students.size();
    }
    
    public int numberOfRestaurants() {
    	return this.restaurants.size();
    }
    
    @Override public HungryStudent getStudent(int id) throws StudentNotInSystemException{
    	for(HungryStudent s : this.students) {
    		if(((HungryStudentImpl)s).getStudId() == id) {
    			return s;
    		}
    	}
    	throw new StudentNotInSystemException();
    }
    
    @Override public Restaurant getRestaurant(int id) throws RestaurantNotInSystemException{
    	for(Restaurant r : this.restaurants) {
    		if(((RestaurantImpl)r).getRestId() == id) {
    			return r;
    		}
    	}
    	throw new RestaurantNotInSystemException();
    }
    
    @Override public HamburgerNetwork addConnection(HungryStudent s1, HungryStudent s2)
            throws StudentNotInSystemException, ConnectionAlreadyExistsException, SameStudentException{
    	if((!students.contains(s1))||(!students.contains(s2))) {
    		throw new StudentNotInSystemException();
    	}
    	if(s1.equals(s2)) {
    		throw new SameStudentException();
    	}
    	if(s1.getFriends().contains(s2) || s2.getFriends().contains(s1)) {

    		throw new ConnectionAlreadyExistsException();
    	}
    	s1.addFriend(s2);
    	s2.addFriend(s1);
    	return this;
    }
    
    
    @Override public Collection<Restaurant> favoritesByRating(HungryStudent s)
            throws StudentNotInSystemException{
    	if(!students.contains(s)) {
    		throw new StudentNotInSystemException();
    	}
    	List<Restaurant> fav = new ArrayList<>();
    	List<HungryStudent> sortedFriends = s.getFriends().stream().sorted().collect(Collectors.toList());
    	for(HungryStudent friend : sortedFriends) {
    		friend.favoritesByRating(0).stream()
    		.filter((element -> (!fav.contains(element)))).collect(Collectors.toList()).forEach(fav::add);
    	}
    	return fav;
    }
    
    @Override public Collection<Restaurant> favoritesByDist(HungryStudent s)
            throws StudentNotInSystemException{
    	if(!students.contains(s)) {
    		throw new StudentNotInSystemException();
    	}
    	List<Restaurant> fav = new ArrayList<>();
    	List<HungryStudent> sortedFriends = s.getFriends().stream().sorted().collect(Collectors.toList());
    	for(HungryStudent friend : sortedFriends) {
    		friend.favoritesByDist(Integer.MAX_VALUE).stream()
    		.filter((element -> (!fav.contains(element)))).collect(Collectors.toList()).forEach(fav::add);
    	}
    	return fav;
    }
    
    private boolean helperRecommend(HungryStudent s, Restaurant r, int t,HungryStudent curr_friend) {
    	if(t <= 0) {
    		return false;
    	}
    	if(!curr_friend.equals(s)){
    		if(curr_friend.favorites().contains(r)) {
    			return true;
    		}
    		for(HungryStudent friend : curr_friend.getFriends() ) {
    			if(helperRecommend(s, r, t-1, friend)) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    @Override public boolean getRecommendation(HungryStudent s, Restaurant r, int t)
            throws StudentNotInSystemException, RestaurantNotInSystemException, ImpossibleConnectionException{
    	if(!students.contains(s)) {
    		throw new StudentNotInSystemException();
    	}
    	if(!restaurants.contains(r)) {
    		throw new RestaurantNotInSystemException();
    	}
    	if(t <0) {
    		throw new ImpossibleConnectionException();
    	}
    	if(s.favorites().contains(r)) {
    		return true;
    	}
    	
    	for(HungryStudent friend : s.getFriends() ) {
			if(helperRecommend(s, r, t, friend)) {
				return true;
			}
		}
    	return false;	
    }
    
    
    @Override public Collection<HungryStudent> registeredStudents(){
    	return students.stream().sorted().collect(Collectors.toList());
    }
    
    @Override public Collection<Restaurant> registeredRestaurants(){
    	return restaurants.stream().sorted().collect(Collectors.toList());
    }

    @Override public String toString() {
    	String students_str = (this.students).stream().sorted()
    			.map(element ->((Integer)(((HungryStudentImpl)element).getStudId())).toString())
    			.collect(Collectors.joining(", ")); 
    	String restaurants_str = (this.restaurants).stream().sorted()
    			.map(element ->((Integer)(((RestaurantImpl)element).getRestId())).toString())
    			.collect(Collectors.joining(", ")); 
    	List<HungryStudent> students_ids = (this.students).stream().sorted()
    			.collect(Collectors.toList()); 
    	String allFriends = "Students:\n";
    	for(HungryStudent s : students_ids) {
    		allFriends += ((Integer)((HungryStudentImpl)s).getStudId()).toString()+" -> ["+
    				s.getFriends().stream().sorted()
    				.map(element ->((Integer)(((HungryStudentImpl)element).getStudId())).toString())
        			.collect(Collectors.joining(", "))+"].\n";    		
    	}
    	return "Registered students: "+students_str+".\nRegistered restaurants: "+restaurants_str+
    			".\n"+allFriends+"End students.";
    }
    
}
