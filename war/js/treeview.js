angular.module('TreeView', []).
controller('TreeController', ['$scope', function($scope){
	$scope.resetTopic = function(obj, depth){
		$scope.stack.splice(depth, $scope.stack.length - depth);
		$scope.setTopic(obj);
	};
	$scope.setTopic = function(obj){
		$scope.stack.push(obj);
		var topic = obj == undefined ? undefined : obj.name;
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
	$scope.stack = new Array();
	$scope.setTopic();
}]);