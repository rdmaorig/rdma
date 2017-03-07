package ie.clients.gdma2.rest;

import ie.clients.gdma2.domain.User;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/user")
public class UserResource extends BaseDataTableResource{

	private static Logger logger = LoggerFactory.getLogger(UserResource.class);

	@RequestMapping("list")
	public List<User> getAllUsers(){
		logger.debug("*** getAllUsers(");
		return serviceFacade.getMetadataService().getAllUsers();
	}

	@RequestMapping("listactive")
	public List<User> getAllActiveUsers(){
		logger.debug("*** getAllUsers(");
		return serviceFacade.getMetadataService().getAllActiveUsers();
	}

	@RequestMapping("table")
	public PaginatedTableResponse<User> getUserPaginatedTable(@RequestParam Map<String, String> params){
		logger.debug("*** getUserPaginatedTable()");
		String orderByColumn = "id";
		
		switch (getOrderByColumn(params)) {
		case 0:
			orderByColumn = "id";
			break;
		case 1:
			orderByColumn = "firstName";
			break;
		case 2:
			orderByColumn = "lastName";
			break;
		case 3:
			orderByColumn = "userName";
			break;
		case 4:
			orderByColumn = "domain";
			break;
		case 5:
			orderByColumn = "admin";
			break;
		case 6:
			orderByColumn = "locked";
			break;
		case 7:
			orderByColumn = "active";
			break;
		}
		
		logger.debug("orderBy column: " + orderByColumn) ;
		PaginatedTableResponse<User> paginatedTabRespUsers= serviceFacade.getMetadataService().getUsers(
				getSearchValue(params), 
				orderByColumn, 
				getOrderByDirection(params),
				getStartIndex(params),
				getLength(params));
		
		paginatedTabRespUsers.setDraw(getDraw(params));
		return paginatedTabRespUsers;
	}

		
	/*see 16.3.1 Communicating errors to the client, 404 instead of 200 with empty body. Empty page... */
	/*	http://localhost:8080/gdma2/rest/user/userName=smith.j	*/
	@RequestMapping(value="/{userName}", method=RequestMethod.GET)
	public ResponseEntity<User> findUserByUserName(@PathVariable String userName) {
		logger.debug("*** findUserByUserName: " + userName);
		List<User> userList = serviceFacade.getMetadataService().findByUserNameIgnoreCase(userName);
		
		User user = null;
		if(userList != null && !userList.isEmpty()){
			logger.debug("user found!");
			user = userList.get(0);
		} else{
			logger.debug("user NOT found!");
		}
		
		//TODO error handling and empty values
		HttpStatus status = user != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<User>(user, status);
	}
	
	/*save/update multiple users*/
	/*this will support single user save/update if sent as array of length 1  [ { user }]*/
	@RequestMapping(value="save", method = RequestMethod.POST)
	public List<User> saveUsers(@RequestBody List<User> users){
		logger.debug("*** saveUsers()");
		return serviceFacade.getMetadataService().saveUsers(users);
	}
	
	/*delete - check if needed possibly only deactivation - which will be done via save/update*/
	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE) 
	public void deleteUser(@PathVariable("id") Integer id){
		logger.debug("*** deleteUser()" + id);
		serviceFacade.getMetadataService().deleteUser(id);
	}
	
}
