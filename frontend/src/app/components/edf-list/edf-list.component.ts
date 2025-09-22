import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Observable } from "rxjs";
import { FileInfo } from "../../models/file-info.model";
import { EdfApiService } from "../../services/edf-api.service";

/**
 * EdfListComponent is responsible for displaying a list of EDF (European Data Format) files and their associated details.
 *
 * This component fetches EDF file data from an API service and processes it for display.
 * The files are provided as an observable stream and can include information such as channels and their attributes.
 *
 * Decorator:
 * - `@Component`: Marks the class as an Angular component and defines its metadata such as selector, template URL, and style URLs.
 *
 * Properties:
 * - `edfFiles$`: An observable stream that emits an array of EDF file information. This data is fetched from the EdfApiService.
 *
 * Constructor:
 * - The constructor initializes the component and injects the EdfApiService to fetch data related to EDF files.
 *
 * Methods:
 * - `ngOnInit`: Lifecycle hook that initializes the observable `edfFiles$` by subscribing to the EdfApiService to fetch the list of EDF files.
 * - `getChannels(file: FileInfo)`: A utility method that takes an EDF file as an argument and returns a string representation of its channels. The channels' names and types are concatenated with their respective details, separated by commas. If a file has no channels, it returns an empty string.
 */
@Component({
    selector: 'app-edf-list',
    templateUrl: './edf-list.component.html',
    styleUrls: ['./edf-list.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class EdfListComponent implements OnInit {
    public edfFiles$!: Observable<FileInfo[]>;

    constructor(private edfApiService: EdfApiService) {
    }

    ngOnInit(): void {
        this.edfFiles$ = this.edfApiService.getEdfFiles();
    }

    getChannels(file: FileInfo): string {
        return file.channels?.map(channel => `${channel.name} ${channel.type}`).join(', ') || '';
    }
}
