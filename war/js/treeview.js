angular.module('TreeView', []).
controller('TreeController', ['$scope', function($scope){
	// -- Navigate up topics via breadcrumb navigation
	$scope.resetTopic = function(obj, depth){
		$scope.stack.splice(depth, $scope.stack.length - depth);
		$scope.setTopic(obj);
	};
	// -- Initialize buffering & disabling variables
	$scope.ajaxBufferVideos = false;
	$scope.ajaxBufferTopics = false;
	// -- Set current Topic handler
	$scope.setTopic = function(obj){
		$scope.ajaxBufferVideos = true;
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
		var urlVideo = '/api/video' + ( topic == undefined ? "" : '?topic=' + topic);
		$.getJSON(urlVideo, function(cbd){
			$scope.$apply(function(){
				$scope.ajaxBufferVideos = false;
				$scope.videos = cbd;
			});
		});
	};
	// -- Add new Video handler
	$scope.addVideo = function(youtubeId){
		$scope.addVideoStatus = 'loading';
		$.ajax({
			type: 'POST',
			url: '/api/video',
			data: {
				youtubeId: youtubeId,
				topic: $scope.current
			},
			success: function(responseText){
				$scope.$apply(function(){
					$scope.addVideoMessage = responseText;
					$scope.addVideoStatus = 'success';
				});
			},
			error: function(data){
				$scope.$apply(function(){
					$scope.addVideoMessage = data.getResponseHeader('message');
					$scope.addVideoStatus = 'error';
				});
			}
		});
	};
	// -- Add new Topic handler
	$scope.addTopic = function(topicName){
		$scope.addTopicStatus = 'loading';
		$.ajax({
			type: 'POST',
			url: '/api/topic',
			data: {
				topicName: topicName,
				topic: $scope.current
			},
			success: function(responseText){
				$scope.$apply(function(){
					$scope.addTopicStatus = 'success';
				});
			},
			error: function(data){
				$scope.$apply(function(){
					$scope.addTopicMessage = data.getResponseHeader('message');
					$scope.addTopicStatus = 'error';
				});
			}
		});
	};
	// -- Initialize
	$scope.stack = new Array();
	$scope.setTopic();
}]);
