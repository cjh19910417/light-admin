package org.lightadmin.demo.model;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Jian on 15/10/31.
 */
@Entity(name = "t_case")
public class Case extends AbstractEntity {
    /**
     * 案件名称
     */
    @Column(length = 100)
    @NotBlank
    private String caseName;
    /**
     * 案件类别
     */
    @Column(length = 100)
    @NotBlank
    private String caseType;
    /**
     * 发生时间
     */
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate time;
    /**
     * 案件描述
     */
    @Column(length = 1000)
    @NotBlank
    private String caseDescir;

    public Case() {
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public String getCaseDescir() {
        return caseDescir;
    }

    public void setCaseDescir(String caseDescir) {
        this.caseDescir = caseDescir;
    }

    public Case(String caseName, String caseType, LocalDate time, String caseDescir) {

        this.caseName = caseName;
        this.caseType = caseType;
        this.time = time;
        this.caseDescir = caseDescir;
    }
}
