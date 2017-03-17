<#import "base.ftl" as base/>
<@base.page title="Files">
    <div class="page-header">
        <h2>Files</h2>
    </div>

    <a type="button" class="btn btn-primary" href="/files/download">Download</a><br><br>

    <form role="form" method="post" enctype="multipart/form-data" action="/files/upload">
        <input type="file" name="file">
        <input class="btn btn-success" type="submit" value="Upload">
    </form>
</@base.page>
