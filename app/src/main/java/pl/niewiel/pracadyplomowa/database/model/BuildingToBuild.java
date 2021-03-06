package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Table;

@Table
public class BuildingToBuild {
    private long buildingId;
    private long buildId;

    public BuildingToBuild() {
    }

    public BuildingToBuild(Building building, Build build) {
        this.buildingId = building.getmId();
        this.buildId = build.getmId();
    }

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
    }

    public long getBuildId() {
        return buildId;
    }

    public void setBuildId(long buildId) {
        this.buildId = buildId;
    }
}
