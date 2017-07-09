import { TravelCIPage } from './app.po';

describe('travel-ci App', () => {
  let page: TravelCIPage;

  beforeEach(() => {
    page = new TravelCIPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
