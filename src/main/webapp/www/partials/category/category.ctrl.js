appCtrllers
    .controller('catCtrl', ['$scope', 'FinApp', '$stateParams', '$rootScope', '$ionicPopup',
        function ($scope, FinApp, $stateParams, $rootScope, $ionicPopup) {
            var groupId = $stateParams.groupId;
            var start = $rootScope.user.defaultStart;
            var end = $rootScope.user.defaultEnd;
            $scope.doRefresh = function () {
                FinApp.getCategoryReport(groupId, start, end).then(function (res) {
                    $scope.period = res.data.period;
                    $scope.categories = res.data.category;
                    $scope.sumAll = 0;
                    if ($scope.categories) {
                        $scope.categories.forEach(function (t) {
                            $scope.sumAll += t.total;
                        })
                    } else {
                        $scope.categories = [];
                    }
                }).finally(function () {
                    $scope.$broadcast('scroll.refreshComplete');
                })
            }

            $scope.doRefresh();
        }])
