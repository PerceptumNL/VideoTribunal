angular.module('TreeSelect', []).
controller('TreeSelectController', ['$scope', function($scope){
	// -- Navigate up topics via breadcrumb navigation
	$scope.resetTopic = function(obj, depth){
		$scope.stack.splice(depth, $scope.stack.length - depth);
		$scope.setTopic(obj);
	};
	// -- Initialize buffering & disabling variables
	$scope.ajaxBufferTopics = false;
	// -- Set current Topic handler
	$scope.setTopic = function(obj){
		$scope.ajaxBufferTopics = true;
		$scope.topics = [];
		$scope.videos = [];
		$scope.stack.push(obj);
		var topic = obj == undefined ? undefined : obj.name;
		$scope.current = topic;
		var urlTopic = '/api/topic' + ( topic == undefined ? "" : '?topic=' + topic);
		$.getJSON(urlTopic, function(cbd){
			$scope.$apply(function(){
				$scope.topics = cbd;
				$scope.ajaxBufferTopics = false;
			});
		});
	};
	// -- Update topic on video
	$scope.updateTopic = function(){
		$scope.updateTopicStatus = 'loading';
		$.ajax({
			type: 'POST',
			url: '/api/vote-topic',
			data: {
				youtubeId: $("#youtubeId").val(),
				topic: $scope.current
			},
			success: function(responseText){
				$scope.$apply(function(){
					$scope.updateTopicMessage = responseText;
					$scope.updateTopicStatus = 'success';
				});
			},
			error: function(data){
				$scope.$apply(function(){
					$scope.updateTopicMessage = data.getResponseHeader('message');
					$scope.updateTopicStatus = 'error';
				});
			}
		});
	};
	// -- Initialize
	$scope.stack = new Array();
	$scope.setTopic();
}]);