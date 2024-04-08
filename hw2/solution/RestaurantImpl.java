package OOP.Solution;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import OOP.Provided.Restaurant;
import OOP.Provided.HungryStudent;
import OOP.Provided.Restaurant.*;

public class RestaurantImpl implements Restaurant{
    
    private int rest_id;
    private String rest_name;
    private int distFromTech;
    private Set<String> menu;
    private Map<Integer, Integer> ratings;
    private double average;
    private int numOfRatings;

    
    public RestaurantImpl(int id, String name, int distFromTech, Set<String> menu){
        this.rest_id = id;
        this.rest_name = name;
        this.distFromTech = distFromTech;
        this.menu = new HashSet<>();
        (this.menu).addAll(menu);
        this.ratings = new HashMap<>();
        this.average = 0.0;
        this.numOfRatings = 0;
    }

    /**
     * @return the distance from the Technion.*/
    @Override public int distance(){
        return this.distFromTech;
    }

    /**
     * rate the restaurant by a student
     * @return the object to allow concatenation of function calls.
     * @param s - the student rating the restaurant
     * @param r - the rating
     * */
    @Override public Restaurant rate(HungryStudent s, int r) throws RateRangeException{
        if(r<0 || r>5){
            throw new RateRangeException();
        }
        int oldRate;
        HungryStudentImpl s1 = (HungryStudentImpl)s;
        if((this.ratings).containsKey((s1.getStudId()))){
            oldRate = (this.ratings).get(s1.getStudId());
            (this.ratings).remove(s1.getStudId());
            if(this.numOfRatings == 1) {
            	this.average = 0;
            }
            else {
            this.average = (this.average*(double)this.numOfRatings - oldRate)/((double)this.numOfRatings-1.0);
            }
            this.numOfRatings--;
        }
        (this.ratings).put(s1.getStudId(),r);
        this.average = (this.average*this.numOfRatings + r)/(this.numOfRatings+1);
        this.numOfRatings++;
        return this;
    }

    /**
     * @return the number of rating the restaurant has received
     * */
    @Override public int numberOfRates(){
        return this.numOfRatings;
    }

    /**
     * @return the restaurant's average rating
     * */
    @Override public double averageRating(){
        return this.average;
    }

    /**
     * @return the restaurant's description as a string in the following format:
     * <format>
     * Restaurant: <name>.
     * Id: <id>.
     * Distance: <dist>.
     * Menu: <menuItem1, menuItem2, menuItem3...>.
     * </format>
     * Note: Menu items are ordered by lexicographical order, asc.
     *
     * Example:
     *
     * Restaurant: BBB.
     * Id: 1.
     * Distance: 5.
     * Menu: Cola, French Fries, Steak.
     *
     * */

     @Override public String toString(){        
        String menu_str = (this.menu).stream().sorted().collect(Collectors.joining(", "));
        
        return "Restaurant: "+ this.rest_name + ".\nId: "+ this.rest_id + ".\nDistance: "+ this.distFromTech +
        ".\nMenu: "+ menu_str +"."; 

    }
     public int getRestId(){
         return this.rest_id;
     }
     
     public String getName(){
         return this.rest_name;
     }


    public boolean equals(Object o){
        if (!(o instanceof RestaurantImpl))
            return false;
        RestaurantImpl other = (RestaurantImpl)o;
        return (other.getRestId() == this.rest_id);
    }

    public int compareTo(Restaurant r){
    	RestaurantImpl other = (RestaurantImpl)r;
        return this.rest_id - other.getRestId();
    }
    
    public int getStudentRating(int id) {
    	if(this.ratings.containsKey(id)) {
    		return this.ratings.get(id);
    	}
    	return -1;
    }
    
    public int sortByAverage(Restaurant r) {
    	RestaurantImpl other = (RestaurantImpl)r;
        double def = this.average - other.averageRating();
        //return 1;
        if(def > 0) {
        	return -1;
        }
        if(def < 0) {
        	return 1;
        }
        int dest = this.distFromTech - other.distance();
        if(dest >0) {
        	return 1;
        }
        if(dest < 0) {
        	return -1;
        }
        dest = this.rest_id - other.getRestId();
        if(dest >0) {
        	return 1;
        }
        if(dest < 0) {
        	return -1;
        }
        return 0;
        
    }
    
    public int sortByDist(Restaurant r) {
    	RestaurantImpl other = (RestaurantImpl)r;
        int def = this.distFromTech - other.distance();
        if(def > 0) {
        	return 1;
        }
        if(def < 0) {
        	return -1;
        }
        double dest = this.average - other.averageRating();
        if(dest >0) {
        	return -1;
        }
        if(dest < 0) {
        	return 1;
        }
        def = this.rest_id - other.getRestId();
        if(def >0) {
        	return 1;
        }
        if(def < 0) {
        	return -1;
        }
        return 0;
    }
    
    
    @Override
    public int hashCode(){
        return this.rest_id;
    }
    
    
}