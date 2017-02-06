package de.thb.fbi.maus.todo.accessors.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import de.thb.fbi.maus.todo.model.TodoUser;


@Path("/todousers")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface TodoUserCRUDAccessor {
	
	@GET
	public List<TodoUser> readAllItems();

	@GET
	@Path("/{itemId}")
	public TodoUser readItem(@PathParam("itemId")int itemId);
	
	@POST
	public TodoUser createItem(TodoUser item);

	@DELETE
	@Path("/{todoUserId}")
	public boolean deleteItem(@PathParam("todoUserId") int itemId);

	@PUT
	public TodoUser updateItem(TodoUser item);
	
	
}
