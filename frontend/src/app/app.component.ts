import { Component } from '@angular/core';

/**
 * The AppComponent class serves as the root component for the Angular application.
 * It is responsible for initializing and managing the application's root-level data and behavior.
 *
 * Decorator:
 * @Component - Indicates that this class is an Angular component.
 *              Contains metadata for configuring the component.
 *
 * Metadata:
 * - selector: Specifies the custom HTML tag name ('app-root') representing this component.
 * - templateUrl: Specifies the file path ('./app.component.html') of the HTML template for this component.
 * - styleUrls: Specifies the file path(s) ('./app.component.scss') for the styles associated with this component.
 *
 * Properties:
 * - title: A string property that represents the title of the application.
 */
@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {
    title = 'frontend';
}
