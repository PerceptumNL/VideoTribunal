
var TreeKey = "TopicTree";
var Tree = null;

// Show form - Display topics
function TreeLoaded(){
	// ...
}

// Hide form - Display error message
function TreeError(){
	// ...
}

// -- Topic Navigation -----------------

function getTopicChild(topic, value){
	for(i in topic.children){
		var child = topic.children[i];
		if(child["id"] == value){
			return child;
		}
	}
}

function getTopicMap(topic){
	var topics = {};
	for(i in topic.children){
		var child = topic.children[i];
		topics[child.id] = child.title;
	}
	return topics;
}

function getExerciseMap(topic){
	var topics = {};
	for(i in topic.children){
		var child = topic.children[i];
		topics[child.name] = child.display_name;
	}
	return topics;
}

// -- Topic Tree Utilities ---------------

function getTreeCourses(){
	return getTopicMap(Tree);
}

function getTreeTopics(courseId){
	var course = getTopicChild(Tree, courseId);
	return getTopicMap(course);
}

function getTreeExercises(courseId, topicId){
	var course = getTopicChild(Tree, courseId);
	var topic = getTopicChild(course, topicId);
	return getExerciseMap(topic);
}

// --- Load Tree ---------------------------------

if(typeof(window.localStorage) != 'undefined'){ 
	Tree = localStorage.getItem(TreeKey);
}

if(Tree == null){
	$.ajax({
		url: 'http://khan-netherlands.appspot.com/api/v1/topictree',
		dataType: 'json',
		success: function(data){
			Tree = data;
			if(typeof(window.localStorage) != 'undefined'){
				localStorage.setItem(TreeKey, JSON.stringify(Tree));
			}
			TreeLoaded();
		},
		error: TreeError()
	});
}
else{
	Tree = JSON.parse(localStorage.getItem(TreeKey));
	TreeLoaded();
}
