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

import de.thb.fbi.maus.todo.model.Todo;


@Path("/todos")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface TodoCRUDAccessor {
	
	@GET
	public List<Todo> readAllItems();

	@GET
	@Path("/{itemId}")
	public Todo readItem(@PathParam("itemId")int itemId);
	
	
	@POST
	public Todo createItem(Todo item);

	@DELETE
	@Path("/{todoId}")
	public boolean deleteItem(@PathParam("todoId") int itemId);
	
	@DELETE
	public boolean deleteAllItems();

	@PUT
	public Todo updateItem(Todo item);
}
