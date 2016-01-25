<h3>dirPath=${dirPath}</h3>
<h4>dirUrl=${dirUrl}</h4>
<#if dirEntries?size gt 0>
    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Size</th>
            <th>Last Modified</th>
        </tr>
        </thead>
        <tbody>
            <#list dirEntries as dirEntry>
            <tr>
                <td><a href="${dirEntry.url}">${dirEntry.name}</a></td>
                <td>
                    <#if dirEntry.file>
                        <code>${dirEntry.size}</code>
                    </#if>
                </td>
                <td>${dirEntry.lastModified?date}</td>
            </tr>
            </#list>
        </tbody>
        <tfoot>
        <tr>
            <td>
                <#if numFiles gt 0>
                ${numFiles} file(s)
                </#if>
                <#if numDirs gt 0>
                ${numDirs} dir(s)
                </#if>
            </td>
            <td colspan="2">
                <#if diskUsage gt 0>
                    <code>${diskUsage}</code> bytes
                </#if>
            </td>
        </tr>
        </tfoot>
    </table>
<#else>
    <p>There are no available files.</p>
</#if>
