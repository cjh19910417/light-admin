package org.lightadmin.demo.config;

import org.lightadmin.api.config.AdministrationConfiguration;
import org.lightadmin.api.config.builder.*;
import org.lightadmin.api.config.unit.*;
import org.lightadmin.api.config.utils.DomainTypePredicates;
import org.lightadmin.api.config.utils.DomainTypeSpecification;
import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.lightadmin.api.config.utils.ScopeMetadataUtils;
import org.lightadmin.demo.config.listener.SimpleRepositoryEventListener;
import org.lightadmin.demo.model.Case;
import org.lightadmin.demo.model.Customer;
import org.lightadmin.demo.service.CustomerService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.lightadmin.api.config.utils.FilterMetadataUtils.filter;
import static org.lightadmin.api.config.utils.ScopeMetadataUtils.all;
import static org.lightadmin.api.config.utils.ScopeMetadataUtils.specification;

/**
 * Created by Jian on 15/10/31.
 */
public class CaseAdministration extends AdministrationConfiguration<Case> {
    public EntityMetadataConfigurationUnit configuration(EntityMetadataConfigurationUnitBuilder configurationBuilder) {
        return configurationBuilder
                .nameField("caseName")
                .singularName("案件")
                .pluralName("案件")
                .build();
    }

    public ScreenContextConfigurationUnit screenContext(ScreenContextConfigurationUnitBuilder screenContextBuilder) {
        return screenContextBuilder.screenName("案件管理").build();
    }

    public FieldSetConfigurationUnit listView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("caseName").caption("案件名称")
                .field("caseType").caption("案件类型")
                .field("time").caption("发案时间")
                .field("caseDescir").caption("案件描述")
                .build();
    }

    public FieldSetConfigurationUnit quickView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("caseName").caption("案件名称")
                .field("caseType").caption("案件类型")
                .field("time").caption("发案时间")
                .field("caseDescir").caption("案件描述")
                .build();
    }

    public FieldSetConfigurationUnit showView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("caseName").caption("案件名称")
                .field("caseType").caption("案件类型")
                .field("time").caption("发案时间")
                .field("caseDescir").caption("案件描述")
                .build();
    }

    public FieldSetConfigurationUnit formView(final PersistentFieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("caseName").caption("案件名称")
                .field("caseType").caption("案件类型")
                .field("time").caption("发案时间")
                .field("caseDescir").caption("案件描述")
                .build();
    }

    public ScopesConfigurationUnit scopes(final ScopesConfigurationUnitBuilder scopeBuilder) {
        return scopeBuilder
                .scope("All", all()).defaultScope().build();
                /*.scope("Buyers", ScopeMetadataUtils.filter(DomainTypePredicates.alwaysTrue()))
                .scope("Sellers", specification(customerNameEqDave())).build();*/
    }

    public FiltersConfigurationUnit filters(final FiltersConfigurationUnitBuilder filterBuilder) {
        return filterBuilder.filters(
                filter().field("caseName").caption("案件名称").build(),
                filter().field("caseType").caption("案件类型").build(),
                filter().field("time").caption("发案时间").build(),
                filter().field("caseDescir").caption("案件描述").build()
        ).build();
    }

    private DomainTypeSpecification<Customer> customerNameEqDave() {
        return new DomainTypeSpecification<Customer>() {
            @Override
            public Predicate toPredicate(final Root<Customer> root, final CriteriaQuery<?> query, final CriteriaBuilder cb) {
                return cb.equal(root.get("firstname"), "Dave");
            }
        };
    }
}
