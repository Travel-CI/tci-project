import { Injectable } from '@angular/core';
import {Project} from '../models/project';
import {Http, Response} from '@angular/http';
import 'rxjs/add/operator/toPromise';
import {environment} from '../../../environments/environment';

@Injectable()
export class ProjectService {

  constructor(private http: Http) {}

  create(): Promise<Project> {

    return this.http.post('/api/projects/add',
      null
    )
    .toPromise()
    .then((res: Response) => res.json() as Project);
  }

  getAllProjects(): Promise<Project[]> {

    return this.http.get('/api/projects')
      .toPromise()
      .then((res: Response) => res.json() as Project[])
  }
}
