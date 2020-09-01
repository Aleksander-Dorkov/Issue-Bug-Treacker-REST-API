package com.secure.secure_back_end.repositories;

import com.secure.secure_back_end.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>
{
    @Query("select p from projects as p join fetch p.projectManager where p.projectId=:project_id")
    Project getSingle(@Param("project_id") Long id);

    @Query("select p from projects as p join fetch p.projectManager")
    List<Project> getAll();

    @Query("select p from projects as p join fetch p.projectManager as pm where pm.userId=:project_manager_id")
    List<Project> getALlByOwnerId(@Param("project_manager_id") Long id);


    @Query(value = "select pq.project_id from projects_qa as pq where pq.qa_id=:qa_id", nativeQuery = true)
    List<Long> getProjectIdsThatIncludeQA(@Param("qa_id") Long id);

    @Query("select p from projects as p join fetch p.projectManager where p.projectId in :project_ids")
    List<Project> getAllByIdsIn(@Param("project_ids") List<Long> projectIds);

    @Modifying
    @Transactional
    @Query("update projects as p set p.title=:title, p.description=:description where p.id=:id")
    void editProject(@Param("id") Long id, @Param("title") String title, @Param("description") String description);

    @Modifying
    @Transactional
    @Query(value = "insert into projects_developers (project_id, developer_id) values (:project_id, :user_id)", nativeQuery = true)
    void addDevelopersToProject(@Param("project_id") Long projectId, @Param("user_id") Long userId);

    @Modifying
    @Transactional
    @Query(value = "delete from projects_developers where project_id=:project_id and developer_id in :user_id", nativeQuery = true)
    void removeDevelopersFromProject(@Param("project_id") Long projectId, @Param("user_id") List<Long> userId);
}
