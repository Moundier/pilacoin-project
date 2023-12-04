import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { Tab1Component } from "./tab-1/tab-1.component";
import { Tab2Component } from "./tab-2/tab-2.component";
import { Tab3Component } from "./tab-3/tab-3.component";
import { TabsComponent } from "./tabs/tabs.component";
import { PageLoginComponent } from "./page-login/page-login.component";
import { Tab0Component } from "./tab-0/tab-0.component";

const routes: Routes = [
    { path: 'login', component: PageLoginComponent }, 
    {
        path: '',
        redirectTo: 'tabs/tab-1', // Redirect to tab-1 by default
        pathMatch: 'full',
    },
    {
        path: 'tabs',
        component: TabsComponent,
        children: [
            { path: 'tab-0', component: Tab0Component, data: { animation: 'tab-0' } }, // The data property works as ID for routes
            { path: 'tab-1', component: Tab1Component, data: { animation: 'tab-1' } },
            { path: 'tab-2', component: Tab2Component, data: { animation: 'tab-2' } },
            { path: 'tab-3', component: Tab3Component, data: { animation: 'tab-3' } },
        ],
    },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }