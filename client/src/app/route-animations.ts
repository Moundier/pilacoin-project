import {
  trigger,
  transition,
  style,
  query,
  animate,
  keyframes,
  group,
  animateChild,
} from "@angular/animations";

// Basic Animation In Angular

export const fader = trigger('routeAnimations', [
  transition('* <=> *', [
    query(':enter, :leave',  [
      style({
        position: 'absolute',
        left: 0,
        width: '100%',
        opacity: 0,
        transform: 'scale(0) translateY(100%)'
      }),
    ]),
    query(':enter', [
      animate('1200ms ease',
        style({ opacity: 1, transform: 'scale(1) translateY(0)' })
      )
    ]),
  ]),
]);

export const stepper = trigger('routeAnimations', [

]);

export const transformer = trigger('routeAnimations', [

]);

export const slider = trigger('routeAnimations', [

]);
