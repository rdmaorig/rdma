package ie.clients.gdma2.rest;

import ie.clients.gdma2.domain.User;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final String QUERY_PARAM_USER_USERNAME = "username";

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

	/*save/update multiple users*/
	/*this will support single user save/update if sent as array of length 1  [ { user }]*/
	@RequestMapping(value="save", method = RequestMethod.POST)
	public List<User> saveUsers(@RequestBody List<User> users){
		logger.debug("*** saveUsers()");
		//TODO: Empty the password
		return serviceFacade.getMetadataService().saveUsers(users);
	}

	/*delete - check if needed possibly only deactivation - which will be done via save/update*/
	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE) 
	public void deleteUser(@PathVariable("id") Integer id){
		logger.debug("*** deleteUser()" + id);
		serviceFacade.getMetadataService().deleteUser(id);
	}
	
	/*get user by ID: 	http://localhost:8080/gdma2/rest/user/id/4 */
	@RequestMapping(value = "/id/{id}")
	public User getUser(@PathVariable("id") Integer userId ){
		logger.info("getUser: " + userId);
		User u= serviceFacade.getMetadataService().findOneUser(userId);
		u.setPassword("");//Emptying the password
		return u;
	}

	/*get users by username: 	http://localhost:8080/gdma2/rest/user/name?username=Maurice.A.Nagata@mailinator.com	*/
	@RequestMapping(value = "name")
	public List<User> getByUserName(@RequestParam Map<String, String> params){
		String userName = getUsernameValue(params);
		logger.debug("getByUserName: " + userName);
		//TODO make userName unique in DB???
		List<User> usersWithSameUserNameList = serviceFacade.getMetadataService().findByUserNameIgnoreCase(userName);

		logger.info("userList size: " + (usersWithSameUserNameList != null ? usersWithSameUserNameList.size() : "0"));
		for (User user : usersWithSameUserNameList) {
			user.setPassword("");//Emptying the password
			logger.info("user: " + user.getUserName());
		}
		return usersWithSameUserNameList;
	}

	protected String getUsernameValue(Map<String, String> reqParams) {
		String val = (reqParams.get(QUERY_PARAM_USER_USERNAME) == null ? "" : reqParams.get(QUERY_PARAM_USER_USERNAME));
		logger.info(QUERY_PARAM_USER_USERNAME + ": " + val);
		return val;
	}

}
