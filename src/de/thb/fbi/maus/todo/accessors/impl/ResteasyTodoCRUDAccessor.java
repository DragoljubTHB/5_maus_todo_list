package de.thb.fbi.maus.todo.accessors.impl;

import java.util.List;

import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

import de.thb.fbi.maus.todo.accessors.api.TodoCRUDAccessor;
import de.thb.fbi.maus.todo.model.Todo;

public class ResteasyTodoCRUDAccessor implements TodoCRUDAccessor {

	/**
	 * the client via which we access the rest web interface provided by the
	 * server
	 */
	private TodoCRUDAccessor restClient;

	public ResteasyTodoCRUDAccessor(String baseUrl) {

		// create a client for the server-side implementation of the interface
		this.restClient = ProxyFactory.create(TodoCRUDAccessor.class, baseUrl, new ApacheHttpClient4Executor());

	}

	@Override
	public List<Todo> readAllItems() {

		List<Todo> itemlist = restClient.readAllItems();

		return itemlist;
	}

	@Override
	public Todo createItem(Todo item) {

		item = restClient.createItem(item);

		return item;
	}

	@Override
	public boolean deleteItem(int itemId) {

		boolean deleted = restClient.deleteItem(itemId);

		return deleted;
	}

	@Override
	public Todo updateItem(Todo item) {

		item = restClient.updateItem(item);

		return item;
	}

	@Override
	public Todo readItem(int itemId) {
		Todo item = restClient.readItem(itemId);

		return item;
	}

	@Override
	public boolean deleteAllItems() {
		return restClient.deleteAllItems();
	}

}
