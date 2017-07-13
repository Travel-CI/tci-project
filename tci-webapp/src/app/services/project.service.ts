import { Injectable } from '@angular/core';
import {Project} from '../models/project';
import {Http, Headers, RequestOptions, Response} from '@angular/http';
import 'rxjs/add/operator/toPromise';
import {environment} from '../../environments/environment';
import {Build} from '../models/build';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class ProjectService {

  private readonly PROJECT_PROXY_URL = '/api/projects';
  private readonly BUILD_PROXY_URL = '/api/builds';

  constructor(private http: Http) {}

  create(project: Project): Promise<Project> {
    return this.http.post(this.PROJECT_PROXY_URL, project, this.getOptions())
      .toPromise()
      .then((res: Response) => res.json() as Project)
      .catch((err: Error) => this.handleError(err));
  }

  update(project: Project): Promise<Project> {
    return this.http.put(this.PROJECT_PROXY_URL, project, this.getOptions())
      .toPromise()
      .then((res: Response) => res.json() as Project)
      .catch((err: Error) => this.handleError(err));
  }

  getAllProjects(): Observable<Project[]> {
    return this.http.get(this.PROJECT_PROXY_URL)
      .map((res: Response) => res.json() as Project[]);
  }

  getProjectById(id: string): Promise<Project> {
    return this.http.get(this.PROJECT_PROXY_URL + '/' + id)
      .toPromise()
      .then((res: Response) => res.json() as Project)
      .catch((err: Error) => this.handleError(err));
  }

  deleteProjectById(projectId: number): Promise<number> {
    return this.http.delete(this.PROJECT_PROXY_URL + '/' + projectId, this.getOptions())
      .toPromise()
      .then((res: Response) => res.json() as number)
      .catch((err: Error) => this.handleError(err));
  }

  getLastBuildForProject(projectId: number): Promise<Build> {
    return this.http.get(this.BUILD_PROXY_URL + '/last/' + projectId)
      .toPromise()
      .then((res: Response) => res.json() as Build)
      .catch((err: Error) => this.handleError(err));
  }

  startBuildForProject(projectId: number, branch: string): Promise<string> {
    return this.http.get(this.PROJECT_PROXY_URL + '/start/' + projectId + '/' + this.stringToHexa(branch))
      .toPromise()
      .then((res: Response) => res.json() as string)
      .catch((err: Error) => this.handleError(err));
  }

  private getOptions(): RequestOptions  {
    let headers = new Headers({ 'Content-Type': 'application/json' });
    return new RequestOptions({ headers: headers });
  }

  private handleError(err: Error): Promise<Project[]> {
    if (environment.enableDebug)
      console.error(err);

    return Promise.reject(err.message || err);
  }

  private stringToHexa(tmp: string): string {
    let str = '';

    for (let i = 0; i < tmp.length; i++)
      str += tmp[i].charCodeAt(0).toString(16);

    return str;
  }
}
