import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { EdfListComponent } from './components/edf-list/edf-list.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClientModule } from "@angular/common/http";
import { TagModule } from "primeng/tag";
import { SkeletonModule } from "primeng/skeleton";
import { TableModule } from "primeng/table";
import { TooltipModule } from "primeng/tooltip";
import { ToolbarModule } from "primeng/toolbar";
import { AvatarModule } from "primeng/avatar";
import { NgOptimizedImage } from "@angular/common";

/**
 * The AppModule class serves as the root module for the Angular application.
 * It defines the main entry point of the application by bootstrapping
 * the AppComponent and includes all the necessary declarations, imports,
 * and providers required for the application to function.
 *
 * Declarations:
 * - AppComponent: The root component of the application.
 * - EdfListComponent: A component that represents a list view (details unknown from the provided context).
 *
 * Imports:
 * - BrowserModule: Provides services essential to launch and run a browser app.
 * - BrowserAnimationsModule: Supports animations within the application.
 * - HttpClientModule: Provides facilities for making HTTP requests.
 * - TableModule: A module for creating and managing tables.
 * - TagModule: A module for displaying tags.
 * - SkeletonModule: A module for skeleton placeholders to improve UX during loading states.
 * - TooltipModule: Enables tooltip functionality.
 * - ToolbarModule: Provides toolbar functionality and styles.
 * - AvatarModule: Supports avatar displays, typically for user profiles or images.
 * - NgOptimizedImage: Optimizes the usage and loading of images in the Angular app.
 *
 * Bootstrap:
 * - The application initializes by bootstrapping the AppComponent.
 *
 * Providers:
 * - An array reserved for providing dependency injection services within the application. Currently no providers are defined.
 */
@NgModule({
    declarations: [
        AppComponent,
        EdfListComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        BrowserAnimationsModule,
        TableModule,
        TagModule,
        SkeletonModule,
        TooltipModule,
        ToolbarModule,
        AvatarModule,
        NgOptimizedImage
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
