import { Component, OnInit } from '@angular/core';
import { Observable } from "rxjs";
import { FileInfo } from "../../models/file-info.model";
import { EdfApiService } from "../../services/edf-api.service";

@Component({
    selector: 'app-edf-list',
    templateUrl: './edf-list.component.html',
    styleUrls: ['./edf-list.component.scss']
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
