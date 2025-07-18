// default package
// Generated Jul 11, 2025, 9:34:41 AM by Hibernate Tools 5.4.21.Final


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Databasechangelog generated by hbm2java
 */
@Entity
@Table(name="databasechangelog"
    ,schema="public"
)
public class Databasechangelog  implements java.io.Serializable {


     private DatabasechangelogId id;

    public Databasechangelog() {
    }

    public Databasechangelog(DatabasechangelogId id) {
       this.id = id;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="id", column=@Column(name="id", nullable=false) ), 
        @AttributeOverride(name="author", column=@Column(name="author", nullable=false) ), 
        @AttributeOverride(name="filename", column=@Column(name="filename", nullable=false) ), 
        @AttributeOverride(name="dateexecuted", column=@Column(name="dateexecuted", nullable=false, length=29) ), 
        @AttributeOverride(name="orderexecuted", column=@Column(name="orderexecuted", nullable=false) ), 
        @AttributeOverride(name="exectype", column=@Column(name="exectype", nullable=false, length=10) ), 
        @AttributeOverride(name="md5sum", column=@Column(name="md5sum", length=35) ), 
        @AttributeOverride(name="description", column=@Column(name="description") ), 
        @AttributeOverride(name="comments", column=@Column(name="comments") ), 
        @AttributeOverride(name="tag", column=@Column(name="tag") ), 
        @AttributeOverride(name="liquibase", column=@Column(name="liquibase", length=20) ), 
        @AttributeOverride(name="contexts", column=@Column(name="contexts") ), 
        @AttributeOverride(name="labels", column=@Column(name="labels") ), 
        @AttributeOverride(name="deploymentId", column=@Column(name="deployment_id", length=10) ) } )
    public DatabasechangelogId getId() {
        return this.id;
    }
    
    public void setId(DatabasechangelogId id) {
        this.id = id;
    }




}


