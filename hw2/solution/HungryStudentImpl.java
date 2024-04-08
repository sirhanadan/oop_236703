package OOP.Solution;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
/*
import OOP.Provided.HungryStudent.ConnectionAlreadyExistsException;
import OOP.Provided.HungryStudent.SameStudentException;
import OOP.Provided.HungryStudent.UnratedFavoriteRestaurantException;
*/
import OOP.Provided.Restaurant;
import OOP.Provided.HungryStudent;
import OOP.Provided.HungryStudent.*;
import java.util.Collection;
import java.util.Comparator;


public class HungryStudentImpl implements HungryStudent{
	private int stud_id;
	private String stud_name;
	private Set<Restaurant> favorites_r;
	private Set<String> fav_names;
	private Set<HungryStudent> friends;
	
	public HungryStudentImpl(int id, String name) {
		this.stud_id = id;
		this.stud_name = name;
		this.favorites_r = new HashSet<>();
		this.fav_names = new HashSet<>();
		this.friends = new HashSet<>();
	}
	
	@Override public HungryStudent favorite(Restaurant r) throws UnratedFavoriteRestaurantException{
		RestaurantImpl other = (RestaurantImpl)r;
		int rate = other.getStudentRating(this.stud_id);
		if(rate == -1) {
			throw new UnratedFavoriteRestaurantException();
		}
		this.favorites_r.add(r);
		this.fav_names.add(((RestaurantImpl)r).getName());
		return this;
	}
	
	@Override public Collection<Restaurant> favorites(){
    	return this.favorites_r;
    }
	
	public int getStudId(){
        return this.stud_id;
    }
	
	@Override public HungryStudent addFriend(HungryStudent s) throws SameStudentException, ConnectionAlreadyExistsException{
		if(this.stud_id == (((HungryStudentImpl)s).getStudId())) {
			throw new SameStudentException();
		}
		if(this.friends.contains(s)) {
			throw new ConnectionAlreadyExistsException();
		}
		this.friends.add(s);
		return this;
	}
	
	@Override public Set<HungryStudent> getFriends(){
    	return this.friends;
    }
	

	//order is not right
	@Override public Collection<Restaurant> favoritesByRating(int rLimit){
		
		Collection<Restaurant> res = this.favorites_r.stream()
				.sorted((r1, r2) -> ((RestaurantImpl)r1).sortByAverage(r2))
				.filter(element -> (element.averageRating() >= rLimit))
				.collect(Collectors.toList());
		return res;
	}
	
	@Override public Collection<Restaurant> favoritesByDist(int dLimit){
		Collection<Restaurant> res = this.favorites_r.stream()
				.filter(element -> (element.distance() <= dLimit))
				.sorted((r1, r2) -> ((RestaurantImpl)r1).sortByDist(r2))
				.collect(Collectors.toList());
		return res;
	}
	
	
	
	public boolean equals(Object o){
        if (!(o instanceof HungryStudent))
            return false;
        HungryStudentImpl other = (HungryStudentImpl)o;
        return (other.getStudId() == this.stud_id);
    }

    public int compareTo(HungryStudent r){
    	HungryStudentImpl other = (HungryStudentImpl)r;
        return this.stud_id - other.getStudId();
    }
    
    /**
     * @return the students's description as a string in the following format:
     * <format>
     * Hungry student: <name>.
     * Id: <id>.
     * Favorites: <resName1, resName2, resName3...>
     * </format>
     * Note: favorite restaurants are ordered by lexicographical order, asc.
     *
     * Example:
     *
     * Hungry student: Oren.
     * Id: 236703.
     * Favorites: BBB, Burger salon.
     *
     * */

    @Override public String toString() {
    	String fav_str = (this.fav_names).stream().sorted().collect(Collectors.joining(", ")); 
        return "Hungry student: "+ this.stud_name + ".\nId: "+ this.stud_id + ".\nFavorites: "+ fav_str +"."; 
    }
    
    @Override
    public int hashCode(){
        return this.stud_id;
    }
	
	

}
