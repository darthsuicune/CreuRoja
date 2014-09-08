class CreateVehiclePositions < ActiveRecord::Migration
  def change
    create_table :vehicle_positions do |t|
      t.integer, :vehicle_id
      t.float, :latitude
      t.float :longitude

      t.timestamps
    end
  end
end
