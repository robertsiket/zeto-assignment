export interface Channel {
    name: string;
    type: string;
}

export interface FileInfo {
    fileName: string;
    valid: boolean;
    identifier: string;
    recordingDate: string;
    patientName: string;
    channels: Channel[];
    recordingLengthSeconds: number;
    numberOfAnnotations: number;
}