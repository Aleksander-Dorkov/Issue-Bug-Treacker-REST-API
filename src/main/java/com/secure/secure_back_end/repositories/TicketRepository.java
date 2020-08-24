package com.secure.secure_back_end.repositories;

import com.secure.secure_back_end.domain.Ticket;
import com.secure.secure_back_end.domain.enums.Category;
import com.secure.secure_back_end.domain.enums.Priority;
import com.secure.secure_back_end.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>
{

    @Query("select t from tickets t join fetch t.submitter")
    List<Ticket> joinFetchUser();

    @Query("select t from tickets t join fetch t.project")
    List<Ticket> joinFetchProject();

    @Query("select t from tickets t join fetch t.submitter where t.submitter.id=:submitter_id")
    List<Ticket> joinFetchUserBySubId(@Param("submitter_id") Long id);

    @Query("select t from tickets t join fetch t.project where t.submitter.id=:submitter_id")
    List<Ticket> joinFetchProjectBySubId(@Param("submitter_id") Long id);

    @Query("select t from tickets t join fetch t.submitter where t.project.id=:project_id")
    List<Ticket> joinFetchUserByProjectId(@Param("project_id") Long id);

    @Query("select t from tickets t join fetch t.project where t.project.id=:project_id")
    List<Ticket> joinFetchProjectByProjectId(@Param("project_id") Long id);

    @Modifying
    @Transactional
    @Query("update tickets as t set t.title=:title, t.description=:description,t.category=:category,t.priority=:priority, t.status=:status ,t.assignedDeveloper.id=:assignedDeveloperId where t.id=:id")
    void updateTicketManager(@Param(value = "title") String title, @Param(value = "description") String description,
                             @Param(value = "category") Category category, @Param(value = "priority") Priority priority,
                             @Param(value = "status") Status status, @Param(value = "assignedDeveloperId") Long assignedDeveloperId,
                             @Param(value = "id") Long id);

    @Modifying
    @Transactional
    @Query("update tickets as t set t.title=:title, t.description=:description, t.category=:category, t.priority=:priority where t.id=:id")
    void updateTicketDev(@Param(value = "title") String title, @Param(value = "description") String description,
                         @Param(value = "category") Category category, @Param(value = "priority") Priority priority,
                         @Param(value = "id") Long id);
}
