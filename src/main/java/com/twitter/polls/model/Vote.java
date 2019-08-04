package com.twitter.polls.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.twitter.polls.model.audit.DateAudit;

@Entity
@Table(name = "votes", uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				"poll_id",
				"user_id"
		})
})
public class Vote extends DateAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "choice_id", nullable = false)
	private Poll poll;
}
