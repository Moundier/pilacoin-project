import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Tab0Component } from './tab-0.component';

describe('Tab0Component', () => {
  let component: Tab0Component;
  let fixture: ComponentFixture<Tab0Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Tab0Component]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(Tab0Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
