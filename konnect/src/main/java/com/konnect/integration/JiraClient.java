package com.konnect.integration;


import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import java.net.URI;

public class JiraClient {
	String username;
	String password;
	String jiraUrl;
	JiraRestClient restClient;
	
	public JiraClient(String username, String password, String jiraUrl) {
	    this.username = username;
	    this.password = password;
	    this.jiraUrl = jiraUrl;
	    this.restClient = getJiraRestClient();
	}
	
	private JiraRestClient getJiraRestClient() {
	    return new AsynchronousJiraRestClientFactory()
	      .createWithBasicHttpAuthentication(getJiraUri(), this.username, this.password);
	}
	
	private URI getJiraUri() {
	    return URI.create(this.jiraUrl);
	}

	public String createIssue(String project, Long key, String summary, String description) throws Exception{
	        final URI jiraServerUri = new URI(jiraUrl);
	        JiraRestClientFactory restClientFactory = new AsynchronousJiraRestClientFactory();
	        JiraRestClient restClient = restClientFactory.createWithBasicHttpAuthentication(jiraServerUri, username, password);
	        IssueInputBuilder issueBuilder = new IssueInputBuilder(project, key, summary);
	        issueBuilder.setDescription(description);  
	        IssueType it = new IssueType(jiraServerUri, key, summary, false, description, null);
	        issueBuilder.setIssueType(it);
	        IssueInput issueInput = issueBuilder.build();
	        return (restClient.getIssueClient().createIssue(issueInput).claim().getKey());
	 }
}
