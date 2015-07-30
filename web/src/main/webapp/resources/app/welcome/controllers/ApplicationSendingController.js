angular
    .module('welcomeModule')
    .controller('ApplicationSendingController', ['$scope', '$q', '$state', '$http', '$log',
        'DataReceivingService', 'DataSendingService', '$stateParams', '$window', '$rootScope','$location',

        function ($scope, $q, $state, $http, $log, dataReceivingService, dataSendingService, $stateParams, $window, $rootScope, $location) {
            $scope.isShownForm = true;
            $scope.blockSearchFunctions = false;
            
            function arrayObjectIndexOf(myArray, searchTerm, property) {
                for(var i = 0, len = myArray.length; i < len; i++) {
                    if (myArray[i][property] === searchTerm) return i;
                }
                var elem = {
                		id: length,
                		designation: searchTerm
                }
                myArray.push(elem);
                return (myArray.length-1);
            }
            
            if ($stateParams.verificationId) {
            dataReceivingService.getVerificationById($stateParams.verificationId).then(function (verification) {
            			
            $scope.verification = verification;
            $scope.formData = {};
            $scope.formData.lastName = $scope.verification.data.lastName;
            $scope.formData.firstName = $scope.verification.data.firstName;
            $scope.formData.middleName = $scope.verification.data.middleName;
            $scope.formData.email = $scope.verification.data.email;
            $scope.formData.phone =  $scope.verification.data.phone;
            $scope.formData.flat = $scope.verification.data.flat;
     		 
            $scope.blockSearchFunctions = true;
            dataReceivingService.findAllRegions().then(function(respRegions) {	
            	$scope.regions = respRegions.data;
            	var index = arrayObjectIndexOf($scope.regions,  $scope.verification.data.region, "designation");
                $scope.selectedRegion = $scope.regions[index];
             
                dataReceivingService.findDistrictsByRegionId( $scope.selectedRegion.id)
                .then(function (districts) {
                	$scope.districts = districts.data;
                	var index = arrayObjectIndexOf($scope.districts,  $scope.verification.data.district, "designation");
                	$scope.selectedDistrict = $scope.districts[index];
                	
                	dataReceivingService.findLocalitiesByDistrictId($scope.selectedDistrict.id)
                     .then(function (localities) {
                         $scope.localities = localities.data;
                         var index = arrayObjectIndexOf($scope.localities,  $scope.verification.data.locality, "designation");
                         $scope.selectedLocality = $scope.localities[index];

                         dataReceivingService.findProvidersByDistrict($scope.selectedDistrict.designation)
                         .then(function (providers) {
                             $scope.providers = providers.data;
                             var index = arrayObjectIndexOf($scope.providers,  $scope.verification.data.provider, "designation");
                              $scope.selectedProvider = $scope.providers[index];
                              
                              dataReceivingService.findStreetsByLocalityId( $scope.selectedLocality.id)
                              .then(function (streets) {
                                  $scope.streets = streets.data;
                                	 var index = arrayObjectIndexOf($scope.streets,  $scope.verification.data.street, "designation");
                                	 $scope.selectedStreet = $scope.streets[index];
                                	 
                                	 dataReceivingService.findBuildingsByStreetId( $scope.selectedStreet.id)
                                     .then(function (buildings) {
                                        $scope.buildings = buildings.data;
                                        var index = arrayObjectIndexOf($scope.buildings,  $scope.verification.data.building, "designation");                                         
                                        $scope.selectedBuilding = $scope.buildings[index].designation;

                                         dataReceivingService.findMailIndexByLocality($scope.selectedLocality.designation, $scope.selectedDistrict.id)
                                         .success(function (indexes) {
                                             $scope.indexes = indexes;
                                             $scope.selectedIndex = $scope.indexes[0];
                                             $scope.blockSearchFunctions = false;
                                         });
                                         
                                     });
                              });
                         });
                     });
               });  	
            });
       });
    }    
         
            /**
             * Receives all possible regions.
             */

            $scope.regions = [];
            $scope.receiveRegions = function () {
            dataReceivingService.findAllRegions()
                .success(function (regions) {
                    $scope.regions = regions;
                    $scope.selectedRegion = "";
                    $scope.selectedDistrict = "";
                    $scope.selectedLocality = "";
                    $scope.selectedLocality = "";
                    $log.debug( $scope.selectedRegion);
                    $log.debug( $scope.selectedDistrict);
                    $log.debug( $scope.selectedLocality);
                    $log.debug( $scope.selectedLocality);
                });
             }
            if( !$stateParams.verificationId) {  
            	$scope.receiveRegions();
            }
           
            /**
             * Receives all possible devices.
             */
            $scope.devices = [];
            dataReceivingService.findAllDevices()
                .success(function (devices) {
                    $scope.devices = devices;
                    $log.debug('device');
                    $log.debug(devices);
                    $scope.selectedDevice =[];
                    $log.debug( $scope.selectedCount);
                });
            /**
             * Receives all possible districts.
             * On-select handler in region input form element.
             */
            $scope.receiveDistricts = function (selectedRegion) {
            	if(!$scope.blockSearchFunctions) {	
            		$scope.districts = [];
                dataReceivingService.findDistrictsByRegionId(selectedRegion.id)
                    .success(function (districts) {
                        $scope.districts = districts;
                        $scope.selectedDistrict = "";
                        $scope.selectedLocality = "";
                        $scope.selectedStreet = "";
                    });
            	}
            };
            /**
             * Receives all possible localities.
             * On-select handler in district input form element.
             */
            $scope.receiveLocalitiesAndProviders = function (selectedDistrict) {
            	if(!$scope.blockSearchFunctions) {
            	$scope.localities = [];
                dataReceivingService.findLocalitiesByDistrictId(selectedDistrict.id)
                    .success(function (localities) {
                        $scope.localities = localities;
                        $scope.selectedLocality = "";
                        $scope.selectedStreet = "";
                        $log.debug( "$scope.selectedRegion");
                        $log.debug( $scope.selectedRegion);
                        $log.debug( "$scope.selectedDistrict");
                        $log.debug( $scope.selectedDistrict);
                        $log.debug(" $scope.selectedLocality");
                        $log.debug( $scope.selectedLocality);
                    });

                //Receives providers corresponding this district
                dataReceivingService.findProvidersByDistrict(selectedDistrict.designation)
                    .success(function (providers) {
                    
                        $scope.providers = providers;
                   $scope.selectedProvider=providers[0];


                    });
            	}
            };

            /**
             * Receives all possible indexes by selected locality and district.
             *
             */

        /*    $scope.receiveIndexes=  function (selectedLocality,selectedDistrict){



            }*/
            /**
             * Receives all possible streets.
             * On-select handler in locality input form element
             */
            $scope.receiveStreets = function (selectedLocality ,selectedDistrict) {
            	if(!$scope.blockSearchFunctions) {
            	$scope.streets = [];
                dataReceivingService.findStreetsByLocalityId(selectedLocality.id)
                    .success(function (streets) {
                        $scope.streets = streets;
                        $scope.selectedStreet = "";
                    });
                    $scope.indexes = [];
                    dataReceivingService.findMailIndexByLocality(selectedLocality.designation,selectedDistrict.id)
                        .success(function (indexes) {
                            $scope.indexes = indexes;

                        });
            	}
            };
            /**
             * Receives all possible buildings.
             * On-select handler in street input form element.
             */
            $scope.receiveBuildings = function (selectedStreet) {
            	if(!$scope.blockSearchFunctions) {
            	$scope.buildings = [];
                dataReceivingService.findBuildingsByStreetId(selectedStreet.id)
                    .success(function (buildings) {
                        $scope.buildings = buildings;
                    });
            	}
            };

            /**
             * Sends data to the server where Verification entity will be created.
             * On-click handler in send button.
             */
            $scope.applicationCodes=[];
            $scope.codes=[];
            $scope.sendApplicationData = function () {
 
                $scope.$broadcast('show-errors-check-validity');

                if ($scope.clientForm.$valid) {
                    $scope.isShownForm = false;
                    for ( var i=0; i< $scope.selectedDevice.length;i++){
                    $scope.formData.region = $scope.selectedRegion.designation;

                    $scope.formData.district = $scope.selectedDistrict.designation;
                    $scope.formData.locality = $scope.selectedLocality.designation;
                    $scope.formData.street = $scope.selectedStreet.designation || $scope.selectedStreet;
                    $scope.formData.building = $scope.selectedBuilding.designation || $scope.selectedBuilding;
                    $scope.formData.providerId = $scope.selectedProvider.id;
                    $scope.formData.deviceId = $scope.selectedDevice[i].id;

                    $scope.applicationCodes.push(dataSendingService.sendApplication($scope.formData))
                     //   $scope.applicationCodes=[];
                       // applicationCodes[]=$q.all  dataSendingService.sendApplication($scope.formData)
                       // .success(function (applicationCode) {
                       //     $scope.applicationCode = applicationCode;
                       //   $log.debug($scope.formData);
                       //   $log.debug( $scope.selectedCount);
                      //  });
                     //hide form because application status is shown
   

                    }

                    $q.all($scope.applicationCodes).then(function(values){
                       $log.debug('values');
                        $log.debug(values);

                        for ( var i=0; i< $scope.selectedDevice.length;i++){

                            $scope.codes[i]=values[i].data;

                        }

                        $log.debug('$scope.codes');
                        $log.debug($scope.codes);
                    });

                 }
            };

          
            $scope.closeAlert = function () {
               	$location.path('/resources/app/welcome/views/start.html');
            }
            
            /**
             * Receives all regex for input fields
             * 
             * 
             */
            $scope.selectedCount="2";
            $scope.STREET_REGEX=/^[a-z\u0430-\u044f\u0456\u0457]{1,20}\s([A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20}|[A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20}){1}$/;
            $scope.FIRST_LAST_NAME_REGEX=/^([A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20}|[A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20})$/;
            $scope.MIDDLE_NAME_REGEX=/^[A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20}$/;
            $scope.FLAT_REGEX=/^([1-9]{1}[0-9]{0,3}|0)$/;
            $scope.BUILDING_REGEX=/^[1-9]{1}[0-9]{0,3}([A-Za-z]|[\u0410-\u042f\u0407\u0406\u0430-\u044f\u0456\u0457]){0,1}$/;
            $scope.PHONE_REGEX=/^0[1-9]\d{8}$/;
            $scope.PHONE_REGEX_SECOND=/^0[1-9]\d{8}$/;
            $scope.EMAIL_REGEX=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
          
            $scope.checkboxModel = false;

        }]);
