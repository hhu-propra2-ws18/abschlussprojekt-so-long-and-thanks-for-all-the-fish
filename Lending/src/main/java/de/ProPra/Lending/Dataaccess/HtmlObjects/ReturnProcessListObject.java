package de.ProPra.Lending.Dataaccess.HtmlObjects;

import lombok.Data;

@Data
public class ReturnProcessListObject {
    long returnID;
    long lendingID;
    String returnerName;
    String articleName;
}
