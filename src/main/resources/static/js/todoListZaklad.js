//var staticTasks='[{"id":'+ Date.now()+',"task":"Write Europass CV as web page","isDone":true},{"id":'+ Date.now()+',"task":"Implement TODO list","isDone":false},{"id":'+ Date.now()+',"task":"Do final assignment","isDone":false}]';

//localStorage.setItem("myToDoList",staticTasks);

var staticTasks = localStorage.getItem("myToDoList");

var tasks=JSON.parse(staticTasks);

if(tasks == null) tasks=[];

var taskTemplate =$("#tmplTask").html();

function addTaskToHtml(task){
    if(task){
        $element=$(Mustache.render(taskTemplate,task));

        $("#frmTasks").append($element);

        if(task.isDone){
            $element.removeClass("activeTask");
            $element.addClass("completedTask");
        }

        $element.click(
            function(){
                $(this).toggleClass("activeTask");
                $(this).toggleClass("completedTask");
                for(var i=0, len=tasks.length; i<len; i++){
                    if($(this).attr('data-id') == tasks[i].id){
                        tasks[i].isDone = !tasks[i].isDone;
                    }
                }
            }
        );
    }
}

// --------------------------------------


// 1.nacitanie uloh
for(var i=0, len=tasks.length; i<len; i++){
    addTaskToHtml(tasks[i]);
}
/*
 * //Alternatíva predch. cyklu pre EcmaScript 6 for(var task of tasks){
 * addTaskToHtml(task); }
 */
$("#btAddTask").click(
		function(){
			$newTaskInput = $("#inNewTask");
			var text = $newTaskInput.val().trim();
			if(text == "") return;
			
			var newTask = {
					id:Date.now(),
					task:text,
					isDone:false
			};
			tasks.push(newTask);
			localStorage.setItem("myToDoList", JSON.stringify(tasks));
			addTaskToHtml(newTask);
			$newTaskInput.val("");
			console.log(tasks);
		}
		);

$("#btRemCmpl").click(
		function(){
			tasks = tasks.filter(task => !task.isDone);
			$("#frmTasks").empty();
			for(var i=0, len=tasks.length; i<len;i++){
				addTaskToHtml(tasks[i]);
			}
			localStorage.setItem("myToDoList", JSON.stringify(tasks));
			});