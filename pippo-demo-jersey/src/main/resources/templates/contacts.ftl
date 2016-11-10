<#import "base.ftl" as base/>
<@base.page title="Contacts">
    <div class="page-header">
        <h2><i class="fa fa-users"> Contacts</i></h2>
    </div>

    <table id="contacts" class="table table-striped table-bordered table-hover">
        <thead>
        <tr>
            <th>#</th>
            <th>Name</th>
            <th>Phone</th>
            <th colspan='2'>Address</th>
        </tr>
        </thead>
        <tbody>
            <#list contacts as contact>
            <tr>
                <td>${contact_index + 1}</td>
                <td>${contact.name}</td>
                <td>${contact.phone}</td>
                <td>${contact.address}</td>
            </tr>
            </#list>
        </tbody>
    </table>
</@base.page>
