package de.thb.fbi.maus.todo.accessors.impl;

import java.util.List;

import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

import de.thb.fbi.maus.todo.accessors.api.TodoUserCRUDAccessor;
import de.thb.fbi.maus.todo.model.TodoUser;

public class ResteasyTodoUserCRUDAccessor implements TodoUserCRUDAccessor {

	/**
	 * the client via which we access the rest web interface provided by the
	 * server
	 */
	private TodoUserCRUDAccessor restClient;

	public ResteasyTodoUserCRUDAccessor(String baseUrl) {

		// create a client for the server-side implementation of the interface
		this.restClient = ProxyFactory.create(TodoUserCRUDAccessor.class, baseUrl, new ApacheHttpClient4Executor());

	}

	@Override
	public List<TodoUser> readAllItems() {

		List<TodoUser> itemlist = restClient.readAllItems();

		return itemlist;
	}

	@Override
	public TodoUser createItem(TodoUser item) {

		item = restClient.createItem(item);

		return item;
	}

	@Override
	public boolean deleteItem(int itemId) {

		boolean deleted = restClient.deleteItem(itemId);

		return deleted;
	}

	@Override
	public TodoUser updateItem(TodoUser item) {

		item = restClient.updateItem(item);

		return item;
	}

	@Override
	public TodoUser readItem(int itemId) {
		TodoUser item = restClient.readItem(itemId);

		return item;
	}

}
