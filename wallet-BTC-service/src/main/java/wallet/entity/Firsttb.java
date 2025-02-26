package wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * firstdb generated by hbm2java
 */
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@Table(name = "firstdb", catalog = "firsttb"
)
public class Firsttb implements java.io.Serializable {

  private Integer codigo;
  private String nombre;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "codigo", unique = true, nullable = false)
  public Integer getCodigo() {
    return this.codigo;
  }

  public void setCodigo(Integer codigo) {
    this.codigo = codigo;
  }

  @Column(name = "nombre")
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

}
