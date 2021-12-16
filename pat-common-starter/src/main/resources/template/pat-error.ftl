<#if (aibkError)??>
    ${(aibkError.exception)!""}: ${(aibkError.message)!""}
    <#if (aibkError.stackTraceElements)??>
        <#list aibkError.stackTraceElements?? as temp>
            at ${temp.className}.${temp.methodName}(${temp.fileName}:${temp.lineNumber})
        </#list>
    </#if>
</#if>
