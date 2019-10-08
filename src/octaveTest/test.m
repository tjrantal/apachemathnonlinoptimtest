clear all;
close all;
fclose all;
clc;

addpath('functions');
pkg load optim	%optim package is required for non-linear least squares optimisation

%Define globals that are used in non-linear optimisation
global constants

initCentre = [50+40*rand(),20+40*rand()];
rad = 40+30*rand();
disp(sprintf('X %.1f Y %.1f rad %.1f',initCentre(1),initCentre(2),rad));
noise = 0;
constants = struct();
constants.coordinates = getCircle(initCentre,rad,noise);
figure
plot(constants.coordinates(:,1),constants.coordinates(:,2),'ro','linewidth',3,'linestyle','none');
hold on;
plot(initCentre(1),initCentre(2),'marker','*','color','r','markersize',5,'linewidth',3);


optimised = lsqnonlin(@optimiseCentre,[70,40,40]);
fitCircle = getCircle(optimised(1:2),optimised(3),0);
plot(fitCircle(:,1),fitCircle(:,2),'go','linewidth',3,'linestyle','none');
hold on;
plot(optimised(1),optimised(2),'marker','*','color','g','markersize',5,'linewidth',3);

disp(sprintf('optim X %.1f Y %.1f rad %.1f',optimised(1),optimised(2),optimised(3)));