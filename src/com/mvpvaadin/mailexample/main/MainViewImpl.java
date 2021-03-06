package com.mvpvaadin.mailexample.main;

import com.mvplite.event.EventBus;
import com.mvplite.event.EventHandler;
import com.mvplite.event.ShowViewEvent;
import com.mvplite.view.NavigationController;
import com.mvplite.view.ui.ArrowBreadcrumbElementFactory;
import com.mvplite.view.ui.ArrowSeparatorFactory;
import com.mvplite.view.ui.Breadcrumbs;
import com.mvpvaadin.mailexample.data.User;
import com.mvpvaadin.mailexample.inbox.InboxViewImpl;
import com.mvpvaadin.mailexample.inbox.ShowInboxViewEvent;
import com.mvpvaadin.mailexample.login.LogoutEvent;
import com.mvpvaadin.mailexample.outbox.OutboxViewImpl;
import com.mvpvaadin.mailexample.outbox.ShowOutboxEvent;
import com.mvpvaadin.mailexample.readmail.ReadMailViewImpl;
import com.mvpvaadin.mailexample.readmail.ShowReadMailEvent;
import com.mvpvaadin.mailexample.service.MailService;
import com.mvpvaadin.mailexample.statistics.ShowStatisticsViewEvent;
import com.mvpvaadin.mailexample.statistics.StatisticsViewImpl;
import com.mvpvaadin.mailexample.writemail.ShowWriteMailEvent;
import com.mvpvaadin.mailexample.writemail.WriteMailViewImpl;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Runo;

public class MainViewImpl extends VerticalLayout implements MainView {

	private static final long serialVersionUID = -8755323074558632618L;
	
	private final NavigationController navigationController;
	private final EventBus eventBus;
	private final User user;
	private final MainPresenter presenter;
	private Button inboxButton;
	
	private VerticalLayout subViewContainer;
	
	private Panel startView;
	
	// Subviews
	private StatisticsViewImpl statisticsView;
	private InboxViewImpl inboxView;
	private ReadMailViewImpl readMailView;
	private OutboxViewImpl outboxView;
	
	// Other View
	private WriteMailViewImpl writeMailView;
	
	
	
	public MainViewImpl(User user, EventBus eventBus, 
			NavigationController navigationController, MailService mailService){
		this.eventBus = eventBus;
		this.navigationController = navigationController;
		this.user = user;

		generateUI();
		
		this.presenter = new MainPresenter(this, eventBus, user, mailService);
		
		bind();
		this.setSizeFull();
		
	}
	
	
	private void bind(){
		eventBus.addHandler(this);
	}
	
	
	public String getUriFragment() {
		return "home";
	}
	
	public String getBreadcrumbTitle() {
		return "Home";
	}

	

	
	private void generateUI(){
		HorizontalLayout header = new HorizontalLayout();
		header.setWidth("100%");
		header.addStyleName("header");
		
		Label logoLabel = new Label("&nbsp; Mail MVP Example",Label.CONTENT_XHTML);
		logoLabel.setStyleName("h1");
		
		
		header.addComponent(logoLabel);
		header.setComponentAlignment(logoLabel, Alignment.MIDDLE_LEFT);
		
		Label usernameLabel = new Label("Hi "+user.getEmailAddress());
		
		
		Button logoutButton = new Button("logout");
		logoutButton.setStyleName("default");
		logoutButton.addListener(new ClickListener() {
			
			private static final long serialVersionUID = 5567732768481757445L;

			public void buttonClick(ClickEvent event) {
				eventBus.fireEvent(new LogoutEvent(user));
			}
		});
		
		inboxButton = new Button("inbox");
		inboxButton.setStyleName("default");
		inboxButton.addListener(new ClickListener() {
			
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				eventBus.fireEvent(new ShowInboxViewEvent());
			}
		});
		
		Button outboxButton = new Button("outbox");
		outboxButton.setStyleName("default");
		outboxButton.addListener(new ClickListener() {
			
			private static final long serialVersionUID = 315579969624810150L;

			public void buttonClick(ClickEvent event) {
				eventBus.fireEvent(new ShowOutboxEvent(null));
			}
		});
		
		Button writeMail = new Button("new mail");
		writeMail.setStyleName("default");
		writeMail.addListener(new ClickListener() {
			
			private static final long serialVersionUID = -1959036904032811749L;

			public void buttonClick(ClickEvent event) {
				eventBus.fireEvent(new ShowWriteMailEvent());
			}
		});
		
		Button statisticsButton = new Button("statistics");
		statisticsButton.setStyleName("default");
		statisticsButton.addListener(new ClickListener() {
			
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				eventBus.fireEvent(new ShowStatisticsViewEvent());
			}
		});
		
		HorizontalLayout headerBar = new HorizontalLayout();
		headerBar.setStyleName("headerBar");
		
		headerBar.addComponent(usernameLabel);
		headerBar.addComponent(inboxButton);
		headerBar.addComponent(writeMail);
		headerBar.addComponent(statisticsButton);
		headerBar.addComponent(outboxButton);
		headerBar.addComponent(logoutButton);
		
		headerBar.setComponentAlignment(usernameLabel, Alignment.MIDDLE_CENTER);
		
		
		headerBar.setSizeUndefined();
		headerBar.setSpacing(true);
		
		header.setSpacing(true);
		header.addComponent(headerBar);
		header.setComponentAlignment(headerBar, Alignment.MIDDLE_RIGHT);
		
		
		// add the Breadcrumb
		Breadcrumbs breadcrumbs = new Breadcrumbs(navigationController);
		breadcrumbs.setBreadcrumbElementFactory( new ArrowBreadcrumbElementFactory());
		breadcrumbs.setSeparatorFactory(new ArrowSeparatorFactory());
		//breadcrumbs.setStyleName("breadcrumbs");
		breadcrumbs.setWidth("100%");
		
		
		// subViewContainer is the container where each subview is placed in 
		subViewContainer = new VerticalLayout();
		subViewContainer.setSizeFull();
		subViewContainer.setStyleName("subViewContainer");
		
		this.addComponent(header);
		this.addComponent(breadcrumbs);
		this.addComponent(subViewContainer);
		
		this.setComponentAlignment(header, Alignment.TOP_LEFT);
		this.setComponentAlignment(breadcrumbs, Alignment.TOP_LEFT);
		this.setComponentAlignment(subViewContainer, Alignment.TOP_LEFT);
		
		setExpandRatio(subViewContainer, 1);
		this.setSizeFull();
	}
	
	
	
	
	public void setSubview(Component subview){
		
		subViewContainer.removeAllComponents();
		subViewContainer.addComponent(subview);
		
	}
	
	
	


	public void setInboxUnreadCount(int unread) {
		String caption = "inbox";
		if (unread>0)
			caption = "inbox ("+unread+")";
		inboxButton.setCaption(caption);
	}

	
	public void showStartSubView(){
		if (startView == null){
			startView = new Panel("Welcome");
			startView.setStyleName(Runo.PANEL_LIGHT);
			startView.addStyleName("panelWhite");
			startView.setSizeFull();
			
			startView.addComponent(new Label("Hi "+user.getUsername()+", <br />use the main menu", Label.CONTENT_XHTML));
		}
		
		setSubview(startView);
		navigationController.setCurrentView(this);
		
	}
	
	@EventHandler
	public void onShowStatisticsViewRequired(ShowStatisticsViewEvent e) {
		if (statisticsView == null)
			statisticsView = new StatisticsViewImpl(this, eventBus, user, presenter.getMailService());
		
		statisticsView.getPresenter().refreshStatistics();
		
		setSubview(statisticsView);
		navigationController.setCurrentView(statisticsView);
	}


	public ShowViewEvent getEventToShowThisView() {
		return new ShowMainViewEvent();
	}


	@EventHandler
	public void onReadMailRequired(ShowReadMailEvent e) {
		
		if (readMailView == null)
			readMailView = new ReadMailViewImpl(inboxView, eventBus, user, presenter.getMailService());
		
		readMailView.getPresenter().setMail(e.getMail());
		
		setSubview(readMailView);
		navigationController.setCurrentView(readMailView);
	}


	@EventHandler
	public void onShowInboxViewRequired(ShowInboxViewEvent e) {
		if (inboxView == null)
			inboxView = new InboxViewImpl(this, eventBus, user, presenter.getMailService());
		
		inboxView.getPresenter().refreshMails();
		setSubview(inboxView);
		navigationController.setCurrentView(inboxView);
	}


	@EventHandler
	public void onShowOutboxRequired(ShowOutboxEvent e) {
		if (outboxView == null)
			outboxView = new OutboxViewImpl(eventBus, user,  presenter.getMailService(), navigationController);
		
		outboxView.getPresenter().refreshList();
		outboxView.getPresenter().setPreselectedMail(e.getPreselectedMail());
		setSubview(outboxView);
		navigationController.setCurrentView(outboxView);
	}


	@EventHandler
	public void onShowWriteMailViewRequired(ShowWriteMailEvent e) {
		if (writeMailView == null){
			writeMailView = new WriteMailViewImpl(eventBus, user,  presenter.getMailService());
		}
		
		String receiverMailAddress = e.getReceiverMailAddress();
		
		if (receiverMailAddress != null) 
			writeMailView.setReceiverMailAddress(receiverMailAddress);
		else
			writeMailView.setReceiverMailAddress("");
		
		String subject = e.getSubject();
		if (subject != null)
			writeMailView.setSubject(subject);
		else
			writeMailView.setSubject("");
		
		getWindow().addWindow(writeMailView);
		
	}


	public void showNewMailNotification(String msg) {
		getWindow().showNotification(msg, Notification.TYPE_TRAY_NOTIFICATION);
	}


	
		
	
}
