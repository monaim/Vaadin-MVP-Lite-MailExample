package com.mvpvaadin.mailexample.statistics;

import com.mvplite.event.EventBus;
import com.mvplite.event.ShowViewEvent;
import com.mvplite.view.NavigateableView;
import com.mvpvaadin.mailexample.data.User;
import com.mvpvaadin.mailexample.service.MailService;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

public class StatisticsViewImpl extends Panel implements StatisticsView {
	
	private static final long serialVersionUID = -3900718906900018585L;
	
	private final NavigateableView parent;
	
	private Label unreadCountLabel;
	private Label usernameLabel;
	private Label emailAddressLabel;
	private Label outboxCountLabel;
	
	private final StatisticsPresenter presenter;
	
	public StatisticsViewImpl(NavigateableView parent, EventBus eventBus, 
			User user, MailService service){
		this.parent = parent;
		
		// Important to instantiate all UI components, before creating the presenter, because the presenter set
		generateUI(); 
		
		presenter = new StatisticsPresenter(this, eventBus, user, service);
		
	}
	
	public StatisticsPresenter getPresenter(){
		return presenter;
	}
	
	public String getUriFragment() {
		return "statistics";
	}

	public String getBreadcrumbTitle() {
		return "Statistics";
	}
	
	public void setUnreadMailsCount(int unread) {
		unreadCountLabel.setCaption("You have "+unread+" unread mails in your inbox");
	}


	public void setUsername(String username) {
		usernameLabel.setCaption("Hello "+username);
	}


	public void setEmailAddress(String emailAddress) {
		emailAddressLabel.setCaption("Your email address is "+emailAddress);
	}
	
	
	private void generateUI(){
		setCaption("Statistics");
		
		unreadCountLabel = new Label();
		usernameLabel = new Label();
		emailAddressLabel = new Label();
		outboxCountLabel = new Label();
		
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(usernameLabel);
		layout.addComponent(emailAddressLabel);
		layout.addComponent(unreadCountLabel);
		layout.addComponent(outboxCountLabel);
		
		this.setStyleName(Runo.PANEL_LIGHT);
		this.addStyleName("panelWhite");
		this.setSizeFull();
		this.addComponent(layout);
	}


	public NavigateableView getParentView() {
		return parent;
	}


	public ShowViewEvent getEventToShowThisView() {
		return new ShowStatisticsViewEvent();
	}

	public void setOutboxMailCount(int mails) {
		outboxCountLabel.setCaption("You have "+mails+" mails in your outbox");
	}
	
	
}
