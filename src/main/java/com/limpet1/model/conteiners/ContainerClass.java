package com.limpet1.model.conteiners;

import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class ContainerClass {

    @Id
    public String asset;
    public String totalBalance;
    public String availableBalance;
    public String tiker;
    public String tikerbalanceinUSDT;

    public ContainerClass() {}
}
