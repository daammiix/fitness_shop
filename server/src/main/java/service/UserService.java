package service;

import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

import dto.auth.RegisterDTO;
import dto.user.UsersFilterDTO;
import model.User;
import model.UserType;
import model.customer.Customer;
import model.customer.CustomerType;
import repository.UserRepository;

public class UserService {
    private static final UserRepository userRepository = UserRepository.getInstance();
    public UserService(){}

    public User getUser(String username) {
        for(User u: userRepository.getAll()) {
            if(u.username.equals(username)) {
                return u;
            }
        }
        return null;
    }
    
    public ArrayList<User> getAll() {
    	return userRepository.getAll();
    }
    
    public Customer createNewCustomer(RegisterDTO registerDTO) {
    	Customer newCustomer = null;
    	// Ako username nije unique vratimo null
    	if(!isUsernameUnique(registerDTO.username)) {
    		return newCustomer;
    	}
    	
    	newCustomer = new Customer();
    	newCustomer.username = registerDTO.username;
    	newCustomer.password = registerDTO.password;
    	newCustomer.firstname = registerDTO.firstName;
    	newCustomer.lastname = registerDTO.lastName;
    	newCustomer.dateOfBirth = registerDTO.getParsedDateOfBirth();
    	newCustomer.gender = registerDTO.getResolveGender();
    	newCustomer.points = 0;

		CustomerType customerType = new CustomerType();
		customerType.type = CustomerType.CustomerTypeEnum.BRONZE;
		newCustomer.customerType = customerType;
    	
    	userRepository.addNewUser(newCustomer);
    	
    	return newCustomer;
    }

	public ArrayList<User> getFilteredUsers(UsersFilterDTO dto){
		ArrayList<User> users = getAll();

		if(dto.userFilter.type != null){
			ArrayList<User> temp = new ArrayList<>();
			for(String type: dto.userFilter.type.split(",")){
				temp.addAll(users.stream().filter(user -> user.userType.equals(UserType.valueOf(type.toUpperCase()))).collect(Collectors.toList()));
			}
			users = temp;
		}

		if(dto.customerFilter.type != null){
			ArrayList<User> temp = new ArrayList<>();
			for(String type: dto.customerFilter.type.split(",")){
				temp.addAll(users.stream().filter(user ->
						user instanceof Customer &&
						((Customer) user).customerType != null &&
						((Customer) user).customerType.type.equals(CustomerType.CustomerTypeEnum.valueOf(type.toUpperCase()))).collect(Collectors.toList()));
			}
			users = temp;
		}

		if(!dto.search.username.trim().isEmpty())
			users = new ArrayList<>(users.stream().filter(user ->
					user.username.toLowerCase().contains(dto.search.username.trim().toLowerCase())).collect(Collectors.toList()));
		if(!dto.search.firstname.trim().isEmpty())
			users = new ArrayList<>(users.stream().filter(user ->
					user.firstname.toLowerCase().contains(dto.search.firstname.trim().toLowerCase())).collect(Collectors.toList()));
		if(!dto.search.lastname.trim().isEmpty())
			users = new ArrayList<>(users.stream().filter(user ->
					user.lastname.toLowerCase().contains(dto.search.lastname.trim().toLowerCase())).collect(Collectors.toList()));

		if(dto.sort.type != null){
			if (dto.sort.reverse)   users.sort(User.COMPARATORS.get(dto.sort.type).reversed());
			else                    users.sort(User.COMPARATORS.get(dto.sort.type));
		}

		return users;
	}
    
    private boolean isUsernameUnique(String username) {
		boolean unique = true;
		ArrayList<User> users = new UserService().getAll();
    	for(User u : new UserService().getAll()) {
    		if(u.username.equalsIgnoreCase(username)) {
    			unique = false;
    			break;
    		}
    	}
    	
    	return unique;
    }
}
