import { Injectable } from '@angular/core';
import {Http, Headers, RequestOptions, Response} from "@angular/http";
import {Command} from "../models/command";
import 'rxjs/add/operator/toPromise';
import {environment} from "../../environments/environment";

@Injectable()
export class CommandService {

  private readonly COMMAND_PROXY_URL = '/api/commands';

  constructor(private http: Http) {}

  addNewCommands(commands: Command[]): Promise<Command[]> {
    return this.http.post(this.COMMAND_PROXY_URL, commands, this.getOptions())
      .toPromise()
      .then((res : Response) => res.json() as Command[])
      .catch((err: Error) => this.handleError(err));
  }

  getCommandsByProjectId(id: number): Promise<Command[]> {
    return this.http.get(this.COMMAND_PROXY_URL + '/' + id)
      .toPromise()
      .then((res: Response) => res.json() as Command[])
      .catch((err: Error) => this.handleError(err));
  }

  updateCommandsByProjectId(id: number, commands: Command[]): Promise<Command[]> {
    return this.http.put(this.COMMAND_PROXY_URL + '/' + id, commands, this.getOptions())
      .toPromise()
      .then((res: Response) => res.json() as Command[])
      .catch((err: Error) => this.handleError(err));
  }

  private getOptions(): RequestOptions  {
    let headers = new Headers({ 'Content-Type': 'application/json' });
    return new RequestOptions({ headers: headers });
  }

  private handleError(err: Error): Promise<Command[]> {

    if(environment.enableDebug)
      console.error(err);

    return Promise.reject(err.message || err);
  }
}
