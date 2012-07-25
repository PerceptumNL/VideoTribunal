
var TreeKey = 'TopicTree';
var Tree = null;

function TreeLoaded(){
	$('#feedbackForm').show();

	var selectCourse = $('select[name="course"]');
	var selectTopic = $('select[name="topic"]');
	var selectExercise = $('select[name="exercise"]');

	// --- Display Courses
	var map = getTopicMap(Tree);
	for(i in map){
		selectCourse.append('<option value="' + i + '">' + map[i] + '</option>');
	}

	// --- Display Course Topics
	selectCourse.change(function(data){
		selectTopic.find('option').not('[data-nodelete]').remove();
		var map = getTreeTopics(selectCourse.val());
		for(i in map){
			selectTopic.append('<option value="' + i + '">' + map[i] + '</option>');
		}
	});

	// --- Display Topic Exercises
	selectTopic.change(function(data){
		selectExercise.find('option').not('[data-nodelete]').remove();
		var map = getTreeExercises(selectCourse.val(), selectTopic.val());
		for(i in map){
			selectExercise.append('<option value="' + i + '">' + map[i] + '</option>');
		}
	});

}

function TreeError(){
	$("#errorMessage").show();
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
