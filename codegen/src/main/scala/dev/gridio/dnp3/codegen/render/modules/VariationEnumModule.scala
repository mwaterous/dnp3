package dev.gridio.dnp3.codegen.render.modules

import dev.gridio.dnp3.codegen.model.{AnyVariation, FixedSize, Variation, ObjectGroup}
import dev.gridio.dnp3.codegen.render._

object VariationEnumModule extends Module {

  override def lines(implicit indent: Indentation) : Iterator[String] = {
    enumDefinition ++ space ++ enumImpl
  }

  private def enumImpl(implicit indent: Indentation) : Iterator[String] = {

    bracket("impl Variation") {
      bracket("pub fn lookup(group: u8, var: u8) -> Option<Variation>") {
        bracket("match group") {
          ObjectGroup.all.iterator.flatMap { g =>
            bracketComma(s"${g.group} => match var") {
              g.variations.iterator.flatMap { v =>
                s"${v.variation} => Some(Variation::${v.name}),".eol
              } ++ "_ => None,".eol
            }
          } ++ "_ => None,".eol
        }
      }
    }

  }

  private def enumDefinition(implicit indent: Indentation) : Iterator[String] = {

    def getVariationDefinition(v: Variation) : String = v match {
      case _ =>  s"${v.name}"
    }

    bracket("pub enum Variation") {
      ObjectGroup.allVariations.iterator.flatMap(v => s"${getVariationDefinition(v)},".eol)
    }
  }


}