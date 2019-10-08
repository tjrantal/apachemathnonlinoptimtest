%x = [xCentre,yCentre,radius]
function y = optimiseCentre(x)
	global constants;
	currentR = zeros(size(constants.coordinates,1),1);
	
	for i = 1:length(currentR)
		currentR(i) = sqrt((constants.coordinates(i,1)-x(1)).^2+(constants.coordinates(i,2)-x(2)).^2);
	end
	
	y = currentR-x(3);	%Optimise this sum -> have to evaluate ode at sampling instants
end
