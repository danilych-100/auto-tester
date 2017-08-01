<html>
<head>
    <meta charset="UTF-8"/>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="css/style.css" />
    <script type="text/javascript">
        function down(id){
            for(var index=0;index<2;index++){
                var a = document.getElementById(id+" "+index);
                if ( a.style.display == 'none' )
                    a.style.display = 'block'
                else
                if ( a.style.display == 'block' )
                    a.style.display = 'none';
            }
        };
    </script>
    <script type="text/javascript">
        function printResult(id,flag){
            var result = document.getElementById(id);
            var info = document.createElement("span");
            if(flag){
                info.setAttribute("class","goodresult");
                info.innerHTML = "Тест пройден";
            }
            else {
                info.setAttribute("class","badresult");
                info.innerHTML = "Тест не пройден";
            }
            result.appendChild(info);
        };
    </script>
</head>
<body>
<#if uploadResult??>
<span>${uploadResult}</span>
</#if>
<#if transactionInfos??>
    <#if "${hasNextResponse?c}" == "true">
    <span>Идет ожидание всех ответов. Текущие результаты: </span><br>
    </#if>
    <#list transactionInfos as transactionInfo>
    <ol>
        <li class="scenario">  <u>${transactionInfo.transactionName}</u><br>
            <div class="responseInfo">
                <span id="responseInfo+${transactionInfo?index}"> Результат выполнения: </span>
                <ol class="respList">
                <#list transactionInfo.getResponseInfo()?keys as responseInfo>
                    <#if responseInfo?isLast>
                        <#if transactionInfo.getResponseInfo()[responseInfo].isValid()>
                            <script>
                                printResult("responseInfo+${transactionInfo?index}",true)
                            </script>
                        <#else>
                            <script>
                                printResult("responseInfo+${transactionInfo?index}",false)
                            </script>
                        </#if>
                    </#if>
                    <#if responseInfo?isFirst>
                        <br> <a onclick=down(${transactionInfo?index})> >Подробнее</a>
                    </#if>
                    <li>
                        <ul id="${transactionInfo?index} ${responseInfo?index}" style="display:none">
                            <span><em><u>${responseInfo}:</em></u> </span><br>
                            <#list transactionInfo.getResponseInfo()[responseInfo].getFieldResult()?keys as resultFieldName>
                                <#if transactionInfo.getResponseInfo()[responseInfo].getFieldResult()[resultFieldName]?contains("Пришло")>
                                    <li class="response">
                                        <span class="field">${resultFieldName}:
                                            <span class="badresult">Не совпадает с ожидаемым:</span>
                                        ${transactionInfo.getResponseInfo()[responseInfo].getFieldResult()[resultFieldName]}
                                        </span>
                                    </li>
                                <#else>
                                    <li class="response">
                                        <span class="field">${resultFieldName}:
                                        ${transactionInfo.getResponseInfo()[responseInfo].getFieldResult()[resultFieldName]}
                                        </span>
                                    </li>
                                </#if>
                            </#list>
                        </ul>
                    </li>
                </#list>
                </ol>
            </div>
        </li>
    </ol>
    </#list>
<#else> <span>В логе ничего нет :( </span>
</#if>
    <form action="/newLogger">
        <input type="submit" value="Обновить" />
    </form>
    <form action="/">
        <input type="submit" value="Назад в главное меню" />
    </form>
    <form action="/responseDoc">
        <input type="submit" value="Просмотреть полный ответ" />
    </form>
</body>
</html>
