angular.module('TreeView', []).
controller('TreeController', ['$scope', function($scope){
	$scope.resetTopic = function(obj, depth){
		$scope.stack.splice(depth, $scope.stack.length - depth);
		$scope.setTopic(obj);
	};
	$scope.setTopic = function(obj){
		$scope.stack.push(obj);
		var topic = obj == undefined ? undefined : obj.name;
		$scope.current = topic;
		var urlTopic = '/api/topic' + ( topic == undefined ? "" : '?topic=' + topic);
		$.getJSON(urlTopic, function(cbd){
			$scope.$apply(function(){
				$scope.topics = cbd;
			});
		});
		var urlVideo = '/api/video' + ( topic == undefined ? "" : '?topic=' + topic);
		$.getJSON(urlVideo, function(cbd){
			$scope.$apply(function(){
				$scope.videos = cbd;
			});
		});
	};
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
					$scope.addVideoMessage = data.statusText;
					$scope.addVideoStatus = 'error';
				});
			}
		});
	};
	$scope.stack = new Array();
	$scope.setTopic();
}]);
