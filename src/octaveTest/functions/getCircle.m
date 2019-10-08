function ret = getCircle(cent,rad,noise)
	ret = zeros(8,2);
	for i = 1:size(ret,1)
		ret(i,:) = [cent(1)+rad*cos(i/size(ret,1)*2*pi)+rand()*noise, ...
					cent(2)+rad*sin(i/size(ret,1)*2*pi)+rand()*noise];
	end
end